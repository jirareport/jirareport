# C14 — Spring Boot 3.0 → 3.1 (Spring Cloud 2022.0 stays)

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17.
4. `.draft/TODO.md`: confirm C13 is `[x]`. If not, STOP.

## Change (this commit only)
`build.gradle.kts`:
- `org.springframework.boot` → newest **3.1.x** (e.g. 3.1.12).
- Spring Cloud BOM stays **2022.0.x** (covers SB 3.0 and 3.1) — bump to newest 2022.0 patch.

Do NOT change Kotlin, Gradle, JDK.

## Changelog watch (Spring Boot 3.1)
- `@ServiceConnection` / Testcontainers support added (optional — don't refactor).
- SSL bundles, Docker Compose support (optional).
- Spring Security 6.1: some `authorizeHttpRequests` lambda APIs tweaked — fix if flagged.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`

## Commit
```
git add -A
git commit -m "build: bump Spring Boot 3.0->3.1

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C14 `[ ]`→`[x]` + SHA.
- Print: "C14 SB 3.1 · green · <SHA>".
