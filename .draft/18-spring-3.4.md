# C18 — Spring Boot 3.3 → 3.4 + Spring Cloud 2023.0 → 2024.0 (Moorgate)

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17.
4. `.draft/TODO.md`: confirm C17 is `[x]`. If not, STOP.

## Change (this commit only)
`build.gradle.kts`:
- `org.springframework.boot` → newest **3.4.x**.
- Spring Cloud BOM `2023.0.x` → **2024.0.x** (Moorgate).

Do NOT change Kotlin, Gradle, JDK.

## Changelog watch (Spring Boot 3.4 / Cloud 2024.0)
- Structured logging support; `RestClient`/`RestTemplate` builder changes.
- Micrometer/observation defaults — no tracing here, low risk.
- Spring Cloud 2024.0 OpenFeign: verify client config keys still resolve.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`

## Commit
```
git add -A
git commit -m "build: bump Spring Boot 3.3->3.4 and Spring Cloud 2023.0->2024.0

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C18 `[ ]`→`[x]` + SHA.
- Print: "C18 SB 3.4 / Cloud 2024.0 · green · <SHA>".
