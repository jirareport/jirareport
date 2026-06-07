# C4 — Spring Boot 2.3 → 2.4 + Spring Cloud Hoxton → 2020.0 (Ilford)

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 11` ; `java -version` → 11.
4. `.draft/TODO.md`: confirm C3 is `[x]`. If not, STOP.

## Why
Spring Boot and the Spring Cloud release train must move together. SB 2.4 pairs with Spring
Cloud 2020.0 (Ilford). SB 2.4 still supports Gradle 6.x, so we stay on Gradle 6.6.1 / JDK 11
this step.

## Change (this commit only)
In `build.gradle.kts`:
- `id("org.springframework.boot") version "2.3.5.RELEASE"` → newest **2.4.x** (e.g. 2.4.13).
- Bump `io.spring.dependency-management` to its latest 1.0.x if needed for SB 2.4.
- In `dependencyManagement { imports { mavenBom(...) } }` change the Spring Cloud BOM
  `org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR8` →
  `org.springframework.cloud:spring-cloud-dependencies:2020.0.x` (newest 2020.0 patch).

Do NOT change Kotlin, Gradle, JDK.

## Changelog watch (Spring Boot 2.4 / Cloud 2020.0)
- **Config/property loading changed** (2.4): `spring.config` import-based loading; multi-document
  YAML rules changed. Check `application.yml` / `application-*.yml` for profile-specific docs and
  `spring.profiles` usage — migrate `spring.profiles:` to `spring.config.activate.on-profile:` if
  present.
- **Spring Cloud 2020.0 renamed bootstrap**: `bootstrap` context is now opt-in
  (`spring-cloud-starter-bootstrap`) — only relevant if you use `bootstrap.yml` (check
  `src/main/resources`). Feign and sleuth starters keep their coordinates here.
- Junit vintage already excluded — keep.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`
- Confirm the app context still loads (integration tests cover this).

## Commit
```
git add -A
git commit -m "build: bump Spring Boot 2.3->2.4 and Spring Cloud Hoxton->2020.0

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C4 `[ ]`→`[x]` + SHA.
- Print: "C4 SB 2.4 / Cloud 2020.0 · green · <SHA>".
