# C22 — Spring Boot 3.5 → 4.0.6 + Spring Cloud 2026.0 + Gradle 9.5.1 + JDK 25 + Dockerfile

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 25` ; `java -version` → 25. **JDK-25 switch step.**
4. `.draft/TODO.md`: confirm C21 is `[x]`. If not, STOP.

## Why / caveat (pre-release accepted)
This is the bleeding-edge jump. Spring Boot 4.0 = Spring Framework 7, JSpecify null-safety,
modularized jars, Java 17–26. OpenFeign requires the **Spring Cloud 2026.0 (Paddington)** train,
which may be only milestone/RC at run time — that is accepted. If 2026.0 has no published BOM
yet, use its latest milestone (e.g. `2026.0.0-M#`/`-RC#`) and add the Spring milestone repo:
`maven { url = uri("https://repo.spring.io/milestone") }` in `repositories {}`.

## Change (this commit only)
1. `build.gradle.kts`:
   - `org.springframework.boot` → **4.0.6**.
   - Spring Cloud BOM `2025.0.x` → **2026.0.x** (latest GA, else latest milestone) (Paddington).
   - Add Spring milestone repo if using a pre-release Cloud BOM (remove once GA).
   - `io.spring.dependency-management` → latest compatible (or rely on SB 4 plugin's built-in
     management; keep the plugin for the Cloud BOM import).
   - **detekt** → **2.0.0-alpha** (plugin + `detekt-formatting`). detekt 2.0 is the first line
     supporting recent Kotlin; re-enable the `detekt {}` block if it was deferred. The detekt
     config schema changed — migrate `detekt-config.yml` per the detekt 2.0 migration guide
     (input paths now via `source.setFrom(...)`/source sets; `input`/`config` DSL changed).
   - Bump `jvmToolchain`/`jvmTarget` to 25 if set (SB4 supports up to Java 26).
2. Bump the Gradle wrapper to **9.5.1** via CLI (NOT by editing the properties file — see
   _CONTEXT.md): `./gradlew wrapper --gradle-version 9.5.1 --distribution-type bin`
3. `Dockerfile`: replace BOTH stages `FROM openjdk:13 as builder` and `FROM openjdk:13-alpine`
   with `eclipse-temurin:25-jdk` (builder) and `eclipse-temurin:25-jre` (runtime). Keep the
   build (`./gradlew clean build -x test`), COPY, EXPOSE, and CMD lines.
4. **JSpecify**: SB4 replaces older null-safety annotations with JSpecify. If anything imports
   `org.springframework.lang.Nullable`/`NonNull`, switch to `org.jspecify.annotations.*`. Fix
   only compiler-flagged usages.

## Changelog watch (Spring Boot 4.0 / Framework 7)
- Modularized starters — some classes moved jars; let dependency management resolve, fix
  unresolved imports.
- Servlet/Reactive split tightening; `RestClient` defaults. Feign via Cloud 2026.0.
- Minimum Gradle for SB4 is 8.x+; 9.5.1 is fine.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`
- `./gradlew --version` → Gradle 9.5.1 ; `java -version` → 25.
- `docker build -t jirareport:upgrade .` → succeeds on eclipse-temurin:25.

## Commit
```
git add -A
git commit -m "build!: Spring Boot 3.5->4.0.6, Spring Cloud 2026.0, Gradle 9.5.1, JDK 25

Spring Framework 7 / Jakarta EE 11. Spring Cloud 2026.0 (Paddington) for
OpenFeign (pre-release accepted). Dockerfile -> eclipse-temurin:25.
detekt -> 2.0.0-alpha.

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C22 `[ ]`→`[x]` + SHA. Note exact Cloud 2026.0 version used (GA vs milestone).
- Print: "C22 SB 4.0.6 / Cloud 2026.0 / Gradle 9.5.1 / JDK 25 · green · <SHA>".
