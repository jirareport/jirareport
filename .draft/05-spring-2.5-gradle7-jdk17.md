# C5 — Spring Boot 2.4 → 2.5 + Gradle 6.6.1 → 7.6 + switch to JDK 17 + jvmTarget 11→17

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java install 17` (if missing) then `cli-assistant env java use 17` ;
   `java -version` → 17. **This is the JDK-17 switch step.**
4. `.draft/TODO.md`: confirm C4 is `[x]`. If not, STOP.

## Why (three coupled bumps, intentionally in one commit)
SB 2.5 is the first Spring Boot whose Gradle plugin supports Gradle 7; Gradle 7.3+ is the first
Gradle that runs on JDK 17; Kotlin (already 1.6) is happy on JDK 17. These are mutually
dependent, so they move together. Spring Cloud 2020.0 already covers SB 2.5 — no train change.

## Change (this commit only)
1. Bump the Gradle wrapper to newest 7.6.x (e.g. 7.6.4) via the CLI — do NOT edit
   `gradle-wrapper.properties` with Edit/Write (it's classifier-blocked; see _CONTEXT.md):
   `./gradlew wrapper --gradle-version 7.6.4 --distribution-type bin`
   (run it on the current JDK 11 / gradle 6.6.1; then confirm `./gradlew --version`).
2. `build.gradle.kts`:
   - `org.springframework.boot` → newest **2.5.x** (e.g. 2.5.15).
   - bump `io.spring.dependency-management` to latest 1.0.x compatible with SB 2.5.
   - `jvmTarget = "11"` → `jvmTarget = "17"`.
   - (Optional but preferred) add a Kotlin/Java toolchain so the build is JDK-pinned:
     `kotlin { jvmToolchain(17) }` — only if it builds clean; otherwise leave jvmTarget alone.
3. Gradle 7 strictness: the `Test` task config and `detekt` block should still work; fix any
   removed-API warnings the Gradle 7 plugin surfaces (e.g. `useJUnitPlatform` is fine).

## Changelog watch
- **Gradle 7**: `jcenter()` already removed (C0). `compile`/`testCompile` configurations are
  gone — this project already uses `implementation`/`testImplementation`, so OK. Watch for
  removed `Project.exec`-style APIs in `gradle/tasks/CreateMigration.gradle.kts` (applied from
  `build.gradle.kts`) — fix if Gradle 7 flags it.
- **Spring Boot 2.5**: SQL init properties renamed (`spring.datasource.initialization-mode` →
  `spring.sql.init.mode`) — check `application.yml`. Flyway still auto-runs.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`
- `./gradlew --version` shows Gradle 7.6.x ; `java -version` shows 17.

## Commit
```
git add -A
git commit -m "build: SB 2.4->2.5, Gradle 6.6.1->7.6, switch to JDK 17 (jvmTarget 17)

SB 2.5 is the first Boot with Gradle 7 support; Gradle 7.3+ runs on JDK
17. Coupled bump.

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C5 `[ ]`→`[x]` + SHA.
- Print: "C5 SB 2.5 / Gradle 7.6 / JDK 17 · green · <SHA>".
