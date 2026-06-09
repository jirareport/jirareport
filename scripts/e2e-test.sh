#!/usr/bin/env bash
#
# End-to-end smoke test for jirareport.
#
# Drives a full client lifecycle and asserts an expected HTTP status per step:
#   login -> create/configure board -> configs/holidays -> reports ->
#   user config -> live Jira (projects/issues/periods) -> cleanup.
# Created resources are torn down at the end, even on failure.
#
# Config + credentials come from scripts/.env (gitignored). Bootstrap it once:
#     cp scripts/.env.example scripts/.env && $EDITOR scripts/.env
#
# Credentials reach curl via a stdin config (-K -), so they never appear in
# process args (ps-safe) and are never echoed.
set -uo pipefail

for bin in curl jq; do
  command -v "$bin" >/dev/null 2>&1 || { echo "missing dependency: $bin" >&2; exit 1; }
done

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ENV_FILE="${ENV_FILE:-$SCRIPT_DIR/.env}"
[[ -f "$ENV_FILE" ]] || { echo "Missing $ENV_FILE — cp $SCRIPT_DIR/.env.example $SCRIPT_DIR/.env" >&2; exit 1; }
set -a; . "$ENV_FILE"; set +a

BASE="${BASE:-http://localhost:8080}"
NO_CLEANUP="${NO_CLEANUP:-0}"
: "${JIRA_BASE:?set JIRA_BASE in $ENV_FILE}"
: "${JIRA_USER:?set JIRA_USER in $ENV_FILE}"
: "${JIRA_PASS:?set JIRA_PASS in $ENV_FILE}"
: "${JIRA_BOARD_ID:?set JIRA_BOARD_ID in $ENV_FILE}"

# Board config; overwritten by discovery below.
EXT_ID=1
COL_START=TODO
COL_END=DONE
COL_FLUX='["TODO","DOING","DONE"]'

TOKEN=""
PASS=0; FAIL=0
HDR="$(mktemp)"; BODY="$(mktemp)"
BOARD_ID=""; CLONE_ID=""; LTC_ID=""; DFC_ID=""; HOLIDAY_ID=""

red()   { printf '\033[31m%s\033[0m' "$1"; }
green() { printf '\033[32m%s\033[0m' "$1"; }

# call METHOD PATH EXPECTED_CODE [curl args...]
# Auth header injected from $TOKEN (never printed); body -> $BODY, headers -> $HDR.
call() {
  local method="$1" path="$2" expect="$3"; shift 3
  STATUS=$(curl -sS -X "$method" "$BASE$path" \
    ${TOKEN:+-H "X-Auth-Token: $TOKEN"} \
    -H 'Content-Type: application/json' \
    -D "$HDR" -o "$BODY" -w '%{http_code}' "$@")
  if [[ "$STATUS" == "$expect" ]]; then
    printf '  %s %-6s %-45s -> %s\n' "$(green PASS)" "$method" "$path" "$STATUS"
    PASS=$((PASS+1))
  else
    printf '  %s %-6s %-45s -> %s (expected %s)\n' "$(red FAIL)" "$method" "$path" "$STATUS" "$expect"
    [[ -s "$BODY" ]] && sed 's/^/        /' "$BODY" | head -5
    FAIL=$((FAIL+1))
  fi
}

location_id() {
  grep -i '^location:' "$HDR" | tr -d '\r' | awk '{print $2}' | sed 's#.*/##'
}

cleanup() {
  [[ "$NO_CLEANUP" == "1" ]] && { echo "skip cleanup (NO_CLEANUP=1)"; return; }
  echo
  echo "== cleanup =="
  [[ -n "$HOLIDAY_ID" && -n "$BOARD_ID" ]] && call DELETE "/boards/$BOARD_ID/holidays/$HOLIDAY_ID" 204
  [[ -n "$DFC_ID"     && -n "$BOARD_ID" ]] && call DELETE "/boards/$BOARD_ID/dynamic-field-configs/$DFC_ID" 204
  [[ -n "$LTC_ID"     && -n "$BOARD_ID" ]] && call DELETE "/boards/$BOARD_ID/lead-time-configs/$LTC_ID" 204
  [[ -n "$CLONE_ID" ]] && call DELETE "/boards/$CLONE_ID" 204
  [[ -n "$BOARD_ID" ]] && call DELETE "/boards/$BOARD_ID" 204
  rm -f "$HDR" "$BODY"
}
trap cleanup EXIT

echo "== discovery (Jira board $JIRA_BOARD_ID) =="
if out=$("$SCRIPT_DIR/jira-board-discovery.sh" "$JIRA_BOARD_ID"); then
  eval "$out"
  echo "  EXT_ID=$EXT_ID  start=$COL_START  end=$COL_END"
  echo "  flux=$COL_FLUX"
else
  echo "  discovery failed for board $JIRA_BOARD_ID" >&2
  exit 1
fi

echo
echo "== 0. sanity =="
call GET /actuator/health 200

echo
echo "== 1. login =="
code=$(curl -sS -X POST "$BASE/login" -D "$HDR" -o /dev/null -w '%{http_code}' -K - <<EOF
data-urlencode = "username=$JIRA_USER"
data-urlencode = "password=$JIRA_PASS"
EOF
)
TOKEN=$(grep -i '^x-auth-token:' "$HDR" | tr -d '\r' | sed 's/^[^:]*:[[:space:]]*//')
if [[ "$code" == "200" && -n "$TOKEN" ]]; then
  echo "  $(green PASS) POST /login -> 200 (token captured)"; PASS=$((PASS+1))
else
  echo "  $(red FAIL) POST /login -> $code"; FAIL=$((FAIL+1))
  grep -i '^x-auth-fail-reason:' "$HDR" | sed 's/^/        /'
  exit 1
fi

echo
echo "== 2. create board =="
call POST /boards 201 -d "{\"name\":\"e2e board\",\"externalId\":$EXT_ID}"
BOARD_ID=$(location_id)
echo "  board id = $BOARD_ID"
[[ -n "$BOARD_ID" ]] || { echo "  $(red FAIL) no board id from Location"; exit 1; }
call GET "/boards/owners" 200
call POST "/boards?boardIdToClone=$BOARD_ID" 201
CLONE_ID=$(location_id)
echo "  clone id = $CLONE_ID"

echo
echo "== 3. configure board =="
call PUT "/boards/$BOARD_ID" 204 -d "{
  \"name\":\"e2e board configured\",
  \"startColumn\":\"$COL_START\",
  \"endColumn\":\"$COL_END\",
  \"fluxColumn\":$COL_FLUX,
  \"ignoreWeekend\":true,
  \"impedimentType\":\"FLAG\",
  \"dueDateType\":\"FIRST_AND_LAST_DUE_DATE\",
  \"issuePeriodNameFormat\":\"MONTH_AND_YEAR\"
}"
call GET "/boards/$BOARD_ID" 200
grep -q 'configured' "$BODY" && echo "  $(green OK) config persisted" || echo "  $(red WARN) name not reflected in GET body"

echo
echo "== 4. lead-time config =="
call POST "/boards/$BOARD_ID/lead-time-configs" 201 -d '{"name":"dev","startColumn":"TODO","endColumn":"DONE"}'
LTC_ID=$(location_id)
call GET  "/boards/$BOARD_ID/lead-time-configs" 200
[[ -n "$LTC_ID" ]] && call GET "/boards/$BOARD_ID/lead-time-configs/$LTC_ID" 200
[[ -n "$LTC_ID" ]] && call PUT "/boards/$BOARD_ID/lead-time-configs/$LTC_ID" 204 -d '{"name":"dev (updated)","startColumn":"TODO","endColumn":"DONE"}'

echo
echo "== 5. dynamic field config =="
call POST "/boards/$BOARD_ID/dynamic-field-configs" 201 -d '{"name":"squad","field":"customfield_1001"}'
DFC_ID=$(location_id)
call GET  "/boards/$BOARD_ID/dynamic-field-configs" 200

echo
echo "== 6. holidays =="
call POST "/boards/$BOARD_ID/holidays" 201 -d '{"date":"25/12/2024","description":"Natal"}'
HOLIDAY_ID=$(location_id)
call GET  "/boards/$BOARD_ID/holidays?page=0&size=20" 200
[[ -n "$HOLIDAY_ID" ]] && call GET "/boards/$BOARD_ID/holidays/$HOLIDAY_ID" 200
[[ -n "$HOLIDAY_ID" ]] && call PUT "/boards/$BOARD_ID/holidays/$HOLIDAY_ID" 204 -d '{"date":"25/12/2024","description":"Natal (updated)"}'

echo
echo "== 7. read-only reports (DB) =="
call GET "/boards/$BOARD_ID/issues?startDate=2024-01-01&endDate=2024-12-31" 200
call GET "/boards/$BOARD_ID/issues/filters" 200
call GET "/boards/$BOARD_ID/issues/filters/keys?startDate=2024-01-01&endDate=2024-12-31" 200
call GET "/boards/$BOARD_ID/issue-periods?startDate=2024-01-01&endDate=2024-12-31" 200

echo
echo "== 8. user config =="
call GET "/users/me/configs" 200
call PUT "/users/me/configs" 204 -d '{"state":"SP","city":"ARARAQUARA","leadTimeChartType":"BAR","throughputChartType":"PIE"}'

echo
echo "== 9. auth negative =="
SAVE="$TOKEN"; TOKEN=""
call GET "/boards" 401
TOKEN="$SAVE"

echo
echo "== 10. Jira / calendar backed =="
call GET "/projects" 200
call GET "/projects/$EXT_ID" 200
call GET "/fields" 200
call GET "/boards/$BOARD_ID/statuses" 200
call GET "/boards/$BOARD_ID/estimates?startDate=2024-01-01&endDate=2024-12-31&filter=ESTIMATE" 200
call POST "/boards/$BOARD_ID/holidays?import=true" 201

call POST "/boards/$BOARD_ID/issue-periods" 201 -d '{"startDate":"01/01/2024","endDate":"31/01/2024"}'
PERIOD_ID=$(location_id)
echo "  issue-period id = $PERIOD_ID"
if [[ -n "$PERIOD_ID" ]]; then
  call GET "/boards/$BOARD_ID/issue-periods/$PERIOD_ID" 200
  call GET "/boards/$BOARD_ID/issues?startDate=2024-01-01&endDate=2024-01-31" 200
  ISSUE_ID=$(jq -r '.issues[0].id // empty' "$BODY" 2>/dev/null)
  if [[ -n "$ISSUE_ID" ]]; then
    call GET "/boards/$BOARD_ID/issues/$ISSUE_ID" 200
  else
    echo "  (skip GET issue/{id}: period has no ingested issues)"
  fi
  # PUT reprocesses by delete+recreate, so the period gets a NEW id; re-fetch it.
  call PUT "/boards/$BOARD_ID/issue-periods/$PERIOD_ID" 204
  call GET "/boards/$BOARD_ID/issue-periods?startDate=2024-01-01&endDate=2024-01-31" 200
  PERIOD_ID=$(jq -r '.periods[0].id // empty' "$BODY" 2>/dev/null)
  [[ -n "$PERIOD_ID" ]] && call DELETE "/boards/$BOARD_ID/issue-periods/$PERIOD_ID" 204
fi

echo
echo "==================================="
printf 'Results: %s passed, %s failed\n' "$(green "$PASS")" "$( [[ $FAIL -gt 0 ]] && red "$FAIL" || echo 0)"
[[ $FAIL -eq 0 ]]
