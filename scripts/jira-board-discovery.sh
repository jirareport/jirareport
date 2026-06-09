#!/usr/bin/env bash
#
# Discover board config from a Jira Agile board, for feeding the e2e test.
#
# Given a Jira Agile board id, emits eval-able shell assignments:
#     EXT_ID     numeric project id   (board externalId)
#     COL_START  first board column   (start column)
#     COL_END    last board column    (end column)
#     COL_FLUX   all board columns in order, as a JSON array
#
# Credentials come from scripts/.env (JIRA_USER/JIRA_PASS/JIRA_BASE) and are fed
# to curl via a stdin config, never in argv, never printed.
#
# Usage:
#     eval "$(./scripts/jira-board-discovery.sh 42)"
#     # or, transitively, via: e2e-test.sh (reads JIRA_BOARD_ID from .env)
#
# Note: board COLUMN names are used as Jira status values in the generated JQL.
# That works when each column is named after its status; if your board groups
# multiple/renamed statuses per column, override COL_* manually instead.
set -uo pipefail

for bin in curl jq; do
  command -v "$bin" >/dev/null 2>&1 || { echo "missing dependency: $bin" >&2; exit 1; }
done

BID="${1:?usage: $0 JIRA_BOARD_ID}"

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ENV_FILE="${ENV_FILE:-$SCRIPT_DIR/.env}"
[[ -f "$ENV_FILE" ]] || { echo "Missing $ENV_FILE — cp $SCRIPT_DIR/.env.example $SCRIPT_DIR/.env" >&2; exit 1; }
set -a; . "$ENV_FILE"; set +a

: "${JIRA_BASE:?set JIRA_BASE in $ENV_FILE}"
: "${JIRA_USER:?set JIRA_USER in $ENV_FILE}"
: "${JIRA_PASS:?set JIRA_PASS in $ENV_FILE}"

jget() {
  curl -sS "$JIRA_BASE$1" -K - <<EOF
user = "$JIRA_USER:$JIRA_PASS"
EOF
}

project_id=$(jget "/rest/agile/1.0/board/$BID" | jq -r '.location.projectId // empty')
[[ -n "$project_id" ]] || { echo "No projectId for board $BID (check the id / access)" >&2; exit 1; }

jget "/rest/agile/1.0/board/$BID/configuration" | jq -r --arg pid "$project_id" '
  [.columnConfig.columns[].name] as $cols
  | "EXT_ID=\($pid)",
    "COL_START=\($cols[0]  | @sh)",
    "COL_END=\($cols[-1]   | @sh)",
    "COL_FLUX=\($cols | tojson | @sh)"
'
