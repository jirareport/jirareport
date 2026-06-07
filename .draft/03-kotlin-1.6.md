# C3 — Kotlin 1.5 → 1.6

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 11` ; `java -version` → 11.
4. `.draft/TODO.md`: confirm C2 is `[x]`. If not, STOP.

## Change (this commit only)
In `build.gradle.kts` `plugins {}` bump all three Kotlin plugins `1.5.x` → newest **1.6.x**
(e.g. 1.6.21). Keep stdlib/reflect aligned.

Do NOT change Spring, Gradle, JDK, or jvmTarget. (Kotlin 1.6 is the first Kotlin comfortable on
JDK 17, but we stay on JDK 11 here — the JDK-17 switch happens at C5 alongside Gradle 7.)

## Changelog watch (Kotlin 1.6)
- Stable `suspend` conversions, sealed `when` exhaustiveness now warns/errors — fix any
  non-exhaustive `when` the compiler flags (scope: only what 1.6 newly enforces).
- Builder inference and stdlib tweaks.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`

## Commit
```
git add -A
git commit -m "build: bump Kotlin 1.5.x -> 1.6.x

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C3 `[ ]`→`[x]` + SHA.
- Print: "C3 Kotlin 1.6 · green · <SHA>".
