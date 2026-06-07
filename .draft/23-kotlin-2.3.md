# C23 — Kotlin 2.2 → 2.3.20 (final)

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 25` ; `java -version` → 25.
4. `.draft/TODO.md`: confirm C22 is `[x]`. If not, STOP.

## Change (this commit only)
1. `build.gradle.kts` `plugins {}`: Kotlin plugins `2.2.x` → **2.3.20**. Keep stdlib/reflect
   aligned to 2.3.20.
2. Confirm **detekt 2.0.0-alpha** supports Kotlin 2.3.x (the alpha line tracks Kotlin 2.3.21).
   Bump detekt to the alpha build that targets Kotlin 2.3 if a newer one exists.
3. Gradle stays **9.5.1**. Kotlin 2.3.20 is validated against Gradle ≤9.3, so the plugin may log
   an "unsupported Gradle" warning on 9.5.1 — acceptable as long as `clean build` is green. If a
   hard failure occurs, drop the wrapper to the newest 9.3.x via
   `./gradlew wrapper --gradle-version 9.3.0 --distribution-type bin` (NOT by editing the
   properties file — see _CONTEXT.md) and note it.

## Changelog watch (Kotlin 2.3 / 2.3.20)
- BTA (Build Tools API) JVM compilation default; name-based destructuring. Fix compiler-flagged
  items only.

## Verify (this is the final state — full end-to-end)
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`
- `./gradlew bootRun` → app boots, Flyway runs all 41 migrations clean (Ctrl-C after).
- `docker build -t jirareport:final .` → succeeds.
- End-state assertions (all must hold):
  - `build.gradle.kts`: Kotlin 2.3.20, Spring Boot 4.0.6, Spring Cloud 2026.0, detekt 2.0.0-alpha.
  - `gradle-wrapper.properties`: `gradle-9.5.1`.
  - `grep -rl "javax\." src` → nothing (only `javax.crypto`/`javax.sql` JDK packages may remain;
    confirm those are the only hits with `grep -rho "javax\.[a-z.]*" src | sort -u`).
  - `grep -ri "sleuth\|javafaker\|vladmihalcea\|jcenter" build.gradle.kts src` → nothing.

## Commit
```
git add -A
git commit -m "build: bump Kotlin 2.2->2.3.20 (final target reached)

Stack now at latest: Kotlin 2.3.20, Spring Boot 4.0.6, Spring Cloud
2026.0, Gradle 9.5.1, JDK 25, detekt 2.0.0-alpha.

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C23 `[ ]`→`[x]` + SHA. **Upgrade complete.**
- Print final summary: full version table + all SHAs + "UPGRADE COMPLETE".
