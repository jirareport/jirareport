# C6 — Spring Boot 2.5 → 2.6 + Spring Cloud 2020.0 → 2021.0 (Jubilee)

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17.
4. `.draft/TODO.md`: confirm C5 is `[x]`. If not, STOP.

## Change (this commit only)
In `build.gradle.kts`:
- `org.springframework.boot` → newest **2.6.x** (e.g. 2.6.15).
- Spring Cloud BOM `2020.0.x` → newest **2021.0.x** (Jubilee).
- bump `io.spring.dependency-management` if required by SB 2.6.

Do NOT change Kotlin, Gradle, JDK.

## Changelog watch (Spring Boot 2.6 / Cloud 2021.0)
- **Circular references prohibited by default** in 2.6 — if context fails to start with a
  circular-reference error, the correct fix is to break the cycle; as a scoped stopgap you may
  set `spring.main.allow-circular-references=true` in `application.yml` and note it.
- Ant path matching default changed toward `PathPatternParser` for MVC — our security config
  uses `antMatchers` (still valid in 2.x). No change needed yet.
- Feign in 2021.0: coordinates unchanged (`spring-cloud-starter-openfeign`).

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`

## Commit
```
git add -A
git commit -m "build: bump Spring Boot 2.5->2.6 and Spring Cloud 2020.0->2021.0

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C6 `[ ]`→`[x]` + SHA.
- Print: "C6 SB 2.6 / Cloud 2021.0 · green · <SHA>".
