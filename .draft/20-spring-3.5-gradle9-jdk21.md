# C20 — Spring Boot 3.4 → 3.5 + Spring Cloud 2024.0 → 2025.0 + Gradle 8 → 9 + JDK 17 → 21

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java install 21` (if missing) then `cli-assistant env java use 21` ;
   `java -version` → 21. **JDK-21 switch step.**
4. `.draft/TODO.md`: confirm C19 is `[x]`. If not, STOP.

## Why
Last stable rung before the bleeding 4.0 jump. Move to JDK 21 (LTS) and Gradle 9 here so the
4.0 step (C22) only changes the framework + JDK 25, not the build engine at the same time.

## Change (this commit only)
1. `build.gradle.kts`:
   - `org.springframework.boot` → newest **3.5.x**.
   - Spring Cloud BOM `2024.0.x` → **2025.0.x** (Northfields).
2. Bump the Gradle wrapper to newest **9.x** (e.g. 9.0/9.1) via CLI (NOT by editing the
   properties file — see _CONTEXT.md): `./gradlew wrapper --gradle-version 9.1.0 --distribution-type bin`
   Kotlin 2.1 supports Gradle 9; if the Kotlin plugin warns about an unvalidated Gradle, that's
   acceptable as long as the build is green.
3. If a `jvmToolchain`/`jvmTarget` is set, bump it to 21.

## Changelog watch
- **Gradle 9**: removes long-deprecated APIs; the applied `CreateMigration.gradle.kts` and any
  custom task config may need fixes. Configuration cache stricter.
- **Spring Boot 3.5**: deprecations cleanup; verify property keys.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`
- `./gradlew --version` → Gradle 9.x ; `java -version` → 21.

## Commit
```
git add -A
git commit -m "build: SB 3.4->3.5, Cloud 2024.0->2025.0, Gradle 8->9, JDK 17->21

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C20 `[ ]`→`[x]` + SHA.
- Print: "C20 SB 3.5 / Cloud 2025.0 / Gradle 9 / JDK 21 · green · <SHA>".
