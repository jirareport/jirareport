# C2 — Kotlin 1.4 → 1.5

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 11` ; `java -version` → 11.
4. `.draft/TODO.md`: confirm C1 is `[x]`. If not, STOP.

## Change (this commit only — Kotlin one minor up)
In `build.gradle.kts` `plugins {}` bump ALL three Kotlin plugins from `1.4.0` to the newest
**1.5.x** patch (resolve latest 1.5 patch, e.g. 1.5.32):
- `kotlin("jvm") version "1.5.x"`
- `kotlin("plugin.spring") version "1.5.x"`
- `kotlin("plugin.jpa") version "1.5.x"`

Keep `kotlin-stdlib-jdk8` / `kotlin-reflect` deps resolving to the same version (see the
"Kotlin version override note" in `_CONTEXT.md`; add explicit version pins only if the Spring
BOM forces a mismatch).

Do NOT change Spring, Gradle, JDK, or jvmTarget.

## Changelog watch (Kotlin 1.5)
- New JVM default behavior and stdlib changes; `@JvmDefault` deprecated (we use the
  `-Xjvm-default=all` compiler arg — keep it as-is for now).
- Some stdlib experimental APIs stabilized; follow compiler warnings/errors only within scope.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`

## Commit
```
git add -A
git commit -m "build: bump Kotlin 1.4.0 -> 1.5.x

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C2 `[ ]`→`[x]` + SHA.
- Print: "C2 Kotlin 1.5 · green · <SHA>".
