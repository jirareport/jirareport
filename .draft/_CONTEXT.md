# Shared context — READ THIS FIRST, every task

You are one step in a long, staged dependency upgrade of the **JiraReport** Kotlin/Spring
backend. Each task file is run by a **fresh session with no memory**, so this file holds the
facts every task needs. Read it fully before doing anything.

## Project
- Root: `/Users/leoferreira/workspace/jirareport`
- Build: `build.gradle.kts` (Gradle Kotlin DSL), wrapper `gradle/wrapper/gradle-wrapper.properties`
- 364 Kotlin files, 112 test files. Pure Kotlin (no Java sources).
- Heavy **Spring Cloud OpenFeign** use (Jira + Holiday HTTP clients) — must survive every step.
- JPA + Postgres + Flyway (41 migrations). Integration tests use **Testcontainers** (Postgres).
- One Spring Security config: `src/main/kotlin/br/com/jiratorio/security/WebSecurityConfig.kt`.

## Starting point (before C0)
Kotlin 1.4.0 · Spring Boot 2.3.5 · Spring Cloud Hoxton.SR8 · Gradle 6.6.1 · detekt 1.14.2 ·
`jvmTarget = 13` · dead `jcenter()` repo present.

**VERIFIED: the pristine state is RED and cannot be green on this machine** (see C0 task for the
fix). Three coupled blockers: (1) `jvmTarget=13` bytecode (class v57) won't run on JDK 11 (max
v55), and JDK 13–15 aren't installable while Gradle 6.6.1 can't run on JDK 17+; (2) `jcenter()`
is dead; (3) detekt 1.14.2's transitive `kotlinx-html-jvm:0.7.2` is bintray-only / 404 on Maven
Central. C0 resolves all three. Do not expect any commit before C0 to build.

## Final target (bleeding-latest, pre-release accepted)
Kotlin **2.3.20** · Spring Boot **4.0.6** · Spring Cloud **2026.0 (Paddington)** · Gradle
**9.5.1** · JDK **25** · detekt **2.0.0-alpha** · hibernate-types-52→**hypersistence-utils-hibernate-63**
· javafaker→**datafaker** · wiremock→**org.wiremock:wiremock 3.x** · feign-httpclient→**feign-hc5**.

## Bumping the Gradle wrapper — DO NOT edit the file
`gradle/wrapper/gradle-wrapper.properties` is blocked for the Edit/Write tools in this
environment (an auto-classifier flags `distributionUrl` as supply-chain sensitive; headless
sessions auto-deny it). **Never** use Edit/Write on it. Instead change the wrapper version with
the Gradle CLI, which is an allowed `Bash(./gradlew:*)` action and regenerates the file itself:
```
./gradlew wrapper --gradle-version <X.Y.Z> --distribution-type bin
```
Run it with the CURRENTLY-active gradle/JDK; it rewrites the properties (and wrapper jar/scripts).
The next `./gradlew` invocation downloads the new version. Verify with `./gradlew --version`.

## The golden rules
1. **One step only.** Do exactly what your task file says. Do NOT bump anything the task does
   not name, even if a newer version exists.
2. **Green bar = `./gradlew clean build` passes** (compile + unit + arch + integration tests).
   Docker must be running for integration tests. Never commit on a red build.
3. **Fix only within your step's scope.** If a build break is genuinely caused by a *different*
   version than the one you're bumping, STOP and report — do not pull in extra upgrades.
4. **Check the changelog** for the version you bump and apply the documented breaking changes.

## JDK switching
Use the local tool (do NOT use sdkman/brew, do NOT hardcode `JAVA_HOME`):
```
cli-assistant env java list            # installed
cli-assistant env java install <v>     # if the JDK you need isn't installed
cli-assistant env java use <v>         # eval's export JAVA_HOME/PATH into THIS shell
java -version                          # confirm
```
Installed: 11, 17, 25, 26. Installable LTS: 11, 17, 21, 25. Each task states its JDK.

**CRITICAL — how `use` actually works (verified):** `cli-assistant` is a profile shell
*function* that reads the binary's output and `eval`s `export JAVA_HOME=... / PATH=...` into the
**current shell**. Consequences:
- Run `use` and the gradle command **in the same shell call**, e.g.
  `cli-assistant env java use 11; ./gradlew clean build`. State does NOT persist to a later,
  separate shell — re-run `use` at the top of each command that builds.
- **Never pipe or redirect the `use` line** (`cli-assistant env java use 11 | tail`,
  `... > log`) — that runs the function in a subshell and the export is lost, so `java` falls
  back to the broken `/usr/bin/java` stub ("Unable to locate a Java Runtime").
- Helper: `./.draft/run-build.sh <jdk> [gradle-args]` does this correctly.
- Do NOT export `JAVA_HOME` to a hardcoded `~/.cli-assistant/jdks/<v>/...` path; use `use`.

Note: a JDK's bytecode (`jvmTarget`) must be ≤ the JDK actually running the tests, or test
classes won't load. That's why C0 lowers `jvmTarget` to 11 while on JDK 11.

## Docker
Colima is the runtime. Confirm before integration tests:
```
docker ps        # if it errors: `colima start` then retry
```

## Compatibility ladder (why the order is what it is)
Spring Cloud train ↔ Spring Boot:
Hoxton→2.3 · 2020.0(Ilford)→2.4/2.5 · 2021.0(Jubilee)→2.6/2.7 · 2022.0(Kilburn)→3.0/3.1 ·
2023.0(Leyton)→3.2/3.3 · 2024.0(Moorgate)→3.4 · 2025.0(Northfields)→3.5 · 2026.0(Paddington)→4.0.

Toolchain minimums:
- SB 2.5 → Gradle ≥6.8 (we use 7.6) → unlocks JDK 17.
- Kotlin 1.6 → first Kotlin happy on JDK 17.
- SB 3.0 → Java 17, Gradle ≥7.5, `jakarta.*`, Hibernate 6, Spring Security 6.
- Kotlin 1.9/2.0 → Gradle 8.x.
- SB 4.0 → Kotlin 2.2 baseline, Spring Framework 7, Gradle 9.x, JDK 17–26.
- Kotlin 2.3.20 → Gradle ≤9.3 validated (9.5.1 may warn — accepted) + detekt 2.0.0-alpha.

## Kotlin version override note
Spring Boot's BOM pins `kotlin-stdlib`/`kotlin-reflect`. While Kotlin runs ahead of the BOM,
the Kotlin plugin version in `plugins {}` controls the compiler; the stdlib/reflect deps are
declared explicitly in `dependencies {}` and resolve to the plugin version. Keep them aligned
to the Kotlin plugin version you set. If a BOM pin wins and causes a mismatch, pin
`org.jetbrains.kotlin:kotlin-stdlib` / `kotlin-reflect` to the plugin version.

## Workflow each task
1. `cd /Users/leoferreira/workspace/jirareport`
2. `cli-assistant env java use <JDK for this step>` ; `java -version`
3. Read `.draft/TODO.md`; verify the previous row is checked `[x]`. If not, STOP and report.
4. Apply the change described in your task file (this step only).
5. `docker ps` (start Colima if needed) → `./gradlew clean build` → `./gradlew detekt`.
6. Green only: `git add -A && git commit -m "<message>"` (end message with the trailer below).
7. Edit `.draft/TODO.md`: flip this row `[ ]`→`[x]`, append the commit SHA.
8. Print one-line summary: versions moved · tests passed · SHA.

## Commit message trailer (end every commit body with)
```
Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>
```
Use Conventional Commits subjects (e.g. `build: bump Kotlin 1.4.0 -> 1.5.x`).

## Looking up "latest patch"
Tasks name minor lines (e.g. "Kotlin 1.5.x", "Spring Boot 2.4.x"). Resolve to the newest patch
of that line at run time (mvnrepository / official release pages). Do not jump to a different
minor than the task specifies.
