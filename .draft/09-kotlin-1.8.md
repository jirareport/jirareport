# C9 — Kotlin 1.7 → 1.8

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17.
4. `.draft/TODO.md`: confirm C8 is `[x]`. If not, STOP.

## Change (this commit only)
`build.gradle.kts` `plugins {}`: all three Kotlin plugins `1.7.x` → newest **1.8.x**
(e.g. 1.8.22). Keep stdlib/reflect aligned.

Do NOT change Spring, Gradle, JDK.

## Changelog watch (Kotlin 1.8)
- `kotlin-stdlib-jdk7`/`jdk8` are merged into `kotlin-stdlib` from 1.8 — the explicit
  `kotlin-stdlib-jdk8` dependency still resolves but is redundant. Leave it for now (removing it
  is out of scope; only remove if it causes a duplicate-class error).
- Stable JVM IR backend; `@JvmStatic`/`@JvmField` edge cases. Fix only what the compiler flags.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`

## Commit
```
git add -A
git commit -m "build: bump Kotlin 1.7.x -> 1.8.x

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C9 `[ ]`→`[x]` + SHA.
- Print: "C9 Kotlin 1.8 · green · <SHA>".
