# C8 — Kotlin 1.6 → 1.7

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17.
4. `.draft/TODO.md`: confirm C7 is `[x]`. If not, STOP.

## Change (this commit only)
`build.gradle.kts` `plugins {}`: all three Kotlin plugins `1.6.x` → newest **1.7.x**
(e.g. 1.7.22). Keep stdlib/reflect aligned.

Do NOT change Spring, Gradle, JDK.

## Changelog watch (Kotlin 1.7)
- New `Enum.entries` not yet; builder inference stabilized; some stdlib functions deprecated.
- Opt-in requirement (`@OptIn`) messaging changed — fix any newly-required opt-ins in scope.
- `freeCompilerArgs` `-Xjsr305=strict` and `-Xjvm-default=all` remain valid.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`

## Commit
```
git add -A
git commit -m "build: bump Kotlin 1.6.x -> 1.7.x

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C8 `[ ]`→`[x]` + SHA.
- Print: "C8 Kotlin 1.7 · green · <SHA>".
