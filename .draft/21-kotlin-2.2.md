# C21 — Kotlin 2.1 → 2.2

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 21` ; `java -version` → 21.
4. `.draft/TODO.md`: confirm C20 is `[x]`. If not, STOP.

## Why
Spring Boot 4.0 (next task) has a **Kotlin 2.2 baseline**. Reach 2.2 first so the 4.0 jump
lands on a compatible Kotlin.

## Change (this commit only)
`build.gradle.kts` `plugins {}`: Kotlin plugins `2.1.x` → newest **2.2.x**. Keep stdlib/reflect
aligned. Bump detekt to the newest version supporting Kotlin 2.2 if needed (else leave / keep
deferred).

Do NOT change Spring, Gradle, JDK.

## Changelog watch (Kotlin 2.2)
- Context parameters (preview), stabilized features; few breaking changes. Fix compiler-flagged
  items only.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt` (unless detekt still deferred)

## Commit
```
git add -A
git commit -m "build: bump Kotlin 2.1->2.2 (Spring Boot 4.0 baseline)

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C21 `[ ]`→`[x]` + SHA.
- Print: "C21 Kotlin 2.2 · green · <SHA>".
