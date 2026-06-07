# C15 — Spring Boot 3.1 → 3.2 + Spring Cloud 2022.0 → 2023.0 (Leyton)

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17.
4. `.draft/TODO.md`: confirm C14 is `[x]`. If not, STOP.

## Change (this commit only)
`build.gradle.kts`:
- `org.springframework.boot` → newest **3.2.x** (e.g. 3.2.12).
- Spring Cloud BOM `2022.0.x` → **2023.0.x** (Leyton).
- Ensure Gradle is ≥8 (SB 3.2 requires Gradle 8.3+). If on 8.x already, fine; otherwise bump via
  CLI (NOT by editing the properties file — see _CONTEXT.md):
  `./gradlew wrapper --gradle-version 8.10.2 --distribution-type bin`.

Do NOT change Kotlin or JDK.

## Changelog watch (Spring Boot 3.2 / Cloud 2023.0)
- Virtual threads support (opt-in `spring.threads.virtual.enabled` — leave off).
- RestClient introduced (don't migrate Feign).
- Spring Cloud 2023.0 OpenFeign: config keys under `spring.cloud.openfeign.*` — verify
  `application.yml`.
- JdkClientHttpRequestFactory default changes — only relevant if you build RestTemplates.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`

## Commit
```
git add -A
git commit -m "build: bump Spring Boot 3.1->3.2 and Spring Cloud 2022.0->2023.0

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C15 `[ ]`→`[x]` + SHA.
- Print: "C15 SB 3.2 / Cloud 2023.0 · green · <SHA>".
