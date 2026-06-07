# C19 — Kotlin 2.0 → 2.1

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17.
4. `.draft/TODO.md`: confirm C18 is `[x]`. If not, STOP.

## Change (this commit only)
`build.gradle.kts` `plugins {}`: Kotlin plugins `2.0.x` → newest **2.1.x** (e.g. 2.1.21).
Keep stdlib/reflect aligned. Bump detekt to the newest version supporting Kotlin 2.1 if the
current detekt blocks the build (else leave).

Do NOT change Spring, Gradle, JDK.

## Changelog watch (Kotlin 2.1)
- Stable guard conditions in `when`, multi-dollar interpolation (opt-in). Few breaking changes;
  fix only compiler-flagged items.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt` (unless detekt still deferred)

## Commit
```
git add -A
git commit -m "build: bump Kotlin 2.0->2.1

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C19 `[ ]`→`[x]` + SHA.
- Print: "C19 Kotlin 2.1 · green · <SHA>".
