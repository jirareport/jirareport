# C17 — Spring Boot 3.2 → 3.3 (Spring Cloud 2023.0 stays)

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17.
4. `.draft/TODO.md`: confirm C16 is `[x]`. If not, STOP.

## Change (this commit only)
`build.gradle.kts`:
- `org.springframework.boot` → newest **3.3.x** (e.g. 3.3.x latest).
- Spring Cloud BOM stays **2023.0.x** (covers SB 3.2 and 3.3) — newest 2023.0 patch.

Do NOT change Kotlin, Gradle, JDK.

## Changelog watch (Spring Boot 3.3)
- Deprecations of some auto-config; SSL/observability tweaks. Low-risk for this app.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`

## Commit
```
git add -A
git commit -m "build: bump Spring Boot 3.2->3.3

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C17 `[ ]`→`[x]` + SHA.
- Print: "C17 SB 3.3 · green · <SHA>".
