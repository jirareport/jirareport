# scripts

Manual API smoke tests for jirareport.

| Script | Purpose |
| --- | --- |
| `e2e-test.sh` | Drives a full client lifecycle against a running instance and asserts an HTTP status per step. Self-cleans created resources. Always exercises the live Jira-backed endpoints. |
| `jira-board-discovery.sh` | Resolves a Jira Agile board id to `EXT_ID` + ordered board columns, consumed by `e2e-test.sh`. |

## Setup

Copy the template and fill in your values:

```bash
cp scripts/.env.example scripts/.env
$EDITOR scripts/.env
```

`scripts/.env` is gitignored — credentials never enter the repo. Both scripts read
it and feed credentials to curl via a stdin config (`-K -`), so they never appear
in process args (ps-safe) and are never echoed.

| Key | Meaning |
| --- | --- |
| `BASE` | app base url (default `http://localhost:8080`) |
| `JIRA_BASE` | Jira instance url |
| `JIRA_USER` | Jira user (Cloud: account email) |
| `JIRA_PASS` | Jira password / API token |
| `JIRA_BOARD_ID` | Jira Agile board id; discovery derives `EXT_ID` + columns from it |
| `NO_CLEANUP` | `1` = leave created resources in place |

## Run

Start the app, then:

```bash
./scripts/e2e-test.sh
```

Discovery runs first (board id → `EXT_ID` + ordered columns), then the full
lifecycle including live Jira calls.

Requires `curl` and `jq`.

## Note

Board COLUMN names are used as Jira status values in the generated JQL. That works
when each column is named after its status; if a board groups multiple/renamed
statuses under one column, the issue-period search may return `400` — set
`EXT_ID`/`COL_START`/`COL_END`/`COL_FLUX` in `.env` manually instead.
