# C7 — Spring Boot 2.6 → 2.7 (Spring Cloud 2021.0 stays)

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17.
4. `.draft/TODO.md`: confirm C6 is `[x]`. If not, STOP.

## Change (this commit only)
In `build.gradle.kts`:
- `org.springframework.boot` → newest **2.7.x** (e.g. 2.7.18 — the last 2.x line).
- Spring Cloud BOM stays on **2021.0.x** (it covers SB 2.6 AND 2.7) — bump to newest 2021.0 patch.
- bump `io.spring.dependency-management` to latest 1.0.x if needed.

Do NOT change Kotlin, Gradle, JDK. **This is the last stop before the 3.0 wall** — get it fully
green; the next three tasks (C11–C13) are the hard migration.

## Changelog watch (Spring Boot 2.7)
- Many `WebSecurityConfigurerAdapter` deprecation warnings appear here — **do not** refactor
  security yet (that's C12, where it's removed). Warnings are fine; build must still pass.
- `spring-boot-starter-validation` already explicit — good (2.3+ no longer transitive).
- Auto-configuration moved to `AutoConfiguration.imports` — only affects custom auto-config
  (none here).

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`

## Commit
```
git add -A
git commit -m "build: bump Spring Boot 2.6->2.7 (last 2.x before 3.0 wall)

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C7 `[ ]`→`[x]` + SHA.
- Print: "C7 SB 2.7 · green · <SHA>".
