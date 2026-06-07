# C16 — Kotlin 1.9 → 2.0 (K2 compiler)

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17.
4. `.draft/TODO.md`: confirm C15 is `[x]`. If not, STOP.

## Why
Kotlin 2.0 ships the K2 compiler by default — the biggest Kotlin change in the ladder. Spring
plugins handle K2 fine at this version. This is the true Kotlin major; isolate it in its own
commit.

## Change (this commit only)
1. `build.gradle.kts` `plugins {}`: all three Kotlin plugins `1.9.x` → newest **2.0.x**
   (e.g. 2.0.21). Keep stdlib/reflect aligned.
2. **Compiler flag rename**: in `tasks.withType<KotlinCompile>` the arg `-Xjvm-default=all`
   is replaced by the stable `-Xjvm-default=all` → use `-jvm-default=all` if the compiler now
   rejects the `-X` form. (In Kotlin 2.x the option moved out of experimental; apply whichever
   form the 2.0.x compiler accepts — verify by building. Keep `-Xjsr305=strict`.)
   Note: `kotlinOptions { ... }` may emit a deprecation in favor of the `compilerOptions {}` DSL —
   migrating is optional; do it only if it stays green and within reason.
3. **detekt**: detekt 1.23.x is the line that supports Kotlin 2.0. Bump
   `io.gitlab.arturbosch.detekt` plugin and `detekt-formatting` to the newest 1.23.x that
   supports Kotlin 2.0 (resolve via detekt compatibility table). If detekt cannot support 2.0
   at a stable version, temporarily disable the `detekt` task (comment the `detekt {}` block and
   skip `./gradlew detekt`) and note it — detekt reaches Kotlin-2.3 support only at 2.0.0-alpha
   (handled in C22/C23).

## Changelog watch (Kotlin 2.0 / K2)
- Stricter smart-casts and exhaustiveness; some previously-compiling code now errors. Fix in
  scope. Watch nullability inference around Spring `@Autowired`/platform types.
- `entries` replaces `values()` recommendation (not required).

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt` (unless detekt deferred — then say so)

## Commit
```
git add -A
git commit -m "build: bump Kotlin 1.9->2.0 (K2 compiler)

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C16 `[ ]`→`[x]` + SHA. Note detekt status.
- Print: "C16 Kotlin 2.0 K2 · green · <SHA>".
