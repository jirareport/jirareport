# C10 — Kotlin 1.8 → 1.9 + Gradle 7.6 → 8.x

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17.
4. `.draft/TODO.md`: confirm C9 is `[x]`. If not, STOP.

## Why
Kotlin 1.9 and the upcoming Spring Boot 3.2+ both want Gradle 8. Bump Gradle now while still on
Spring Boot 2.7 (which tolerates running under a Gradle 8 wrapper for a Kotlin-only project —
verify the build; if the SB 2.7 plugin rejects Gradle 8, keep Gradle 7.6 here and move the
Gradle-8 bump into C11). Prefer Gradle 8 here if green.

## Change (this commit only)
1. `build.gradle.kts` `plugins {}`: Kotlin plugins `1.8.x` → newest **1.9.x** (e.g. 1.9.25).
2. Bump the Gradle wrapper to newest **8.x** (e.g. 8.10.x) via CLI (NOT by editing the
   properties file — see _CONTEXT.md): `./gradlew wrapper --gradle-version 8.10.2 --distribution-type bin`

Do NOT change Spring or JDK.

## Changelog watch
- **Gradle 8**: requires JVM 8+ (fine on 17). `archivesBaseName` and some conventions
  deprecated; the `CreateMigration.gradle.kts` applied script may need API fixes. Kotlin DSL
  accessors stricter — fix compile errors in build scripts.
- **Kotlin 1.9**: `Enum.entries` preview, `kotlin-stdlib-jdk8` fully folded in; data object;
  some deprecations promoted to errors. Fix in scope.
- If SB 2.7's Gradle plugin errors on Gradle 8: revert the wrapper to 7.6.x (again via
  `./gradlew wrapper --gradle-version 7.6.4 --distribution-type bin`), commit Kotlin 1.9 only,
  and add a note in TODO that Gradle 8 moves to C11.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`
- `./gradlew --version` shows Gradle 8.x.

## Commit
```
git add -A
git commit -m "build: bump Kotlin 1.8->1.9 and Gradle 7.6->8.x

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C10 `[ ]`→`[x]` + SHA (note if Gradle 8 was deferred).
- Print: "C10 Kotlin 1.9 / Gradle 8 · green · <SHA>".
