# C13 — THE WALL (3/3): Feign HttpClient 5 + test-library bumps

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17.
4. `.draft/TODO.md`: confirm C12 is `[x]`. If not, STOP.

## Part A — Feign Apache HttpClient 4 → 5
SB 3 / Spring Cloud 2022.0 drop Apache HttpClient 4. The project declares
`implementation("io.github.openfeign:feign-httpclient")` (HC4-based).
1. `build.gradle.kts`: replace `feign-httpclient` with `io.github.openfeign:feign-hc5`
   (version managed by the Spring Cloud BOM; otherwise pin latest).
2. Custom Feign code may reference HC4 types — check:
   - `jira/client/extension/RequestExtension.kt`
   - `jira/client/extension/ResponseExtension.kt`
   - `holiday/client/config/HolidayClientConfig.kt`, `jira/client/config/AuthClientConfiguration.kt`
   Replace any `org.apache.http.*` (HC4) usage with `org.apache.hc.*` (HttpClient 5) equivalents.
   If they only use Feign's own `Request`/`Response` types, no change is needed.
3. `application.yml`: any `feign.httpclient.*` keys moved to `spring.cloud.openfeign.httpclient.*`
   in newer Spring Cloud — update if present.

## Part B — test-library bumps for SB3 / JDK17
Bump to latest versions compatible with Spring Boot 3 (resolve newest on mvnrepository):
- `org.testcontainers:postgresql` → latest 1.x (jakarta-friendly).
- `io.mockk:mockk` → latest 1.x.
- `com.tngtech.archunit:archunit` → latest 1.x (0.12.0 is very old; API mostly compatible).
- `io.rest-assured:rest-assured` → managed by SB3 BOM (uses jakarta); ensure it resolves.
- **wiremock**: replace `com.github.tomakehurst:wiremock:2.27.1` with
  `org.wiremock:wiremock-standalone:3.x` (or `org.wiremock:wiremock:3.x`). WireMock 3 is
  jakarta/Jetty-12 based and required for SB3. Update any `com.github.tomakehurst.wiremock.*`
  imports to `com.github.tomakehurst.wiremock.*` (WireMock 3 kept the package) — fix per compiler.

## Changelog watch
- ArchUnit 0.12→1.x: a few `ArchRule` APIs renamed; fix the arch test (`@Tag("arch")`).
- WireMock 3: `WireMockServer`/`stubFor` API stable; Jetty handler internals changed (rarely
  user-facing).

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`
- All three test tags run: unit, integration, arch. The Feign clients hit Jira/Holiday — their
  integration tests (WireMock) must pass.

## Commit
```
git add -A
git commit -m "build!: Feign HttpClient5, WireMock 3, and SB3-compatible test libs

Completes the Spring Boot 3.0 migration (final of three commits).

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C13 `[ ]`→`[x]` + SHA. **The 2.7→3.0 wall is now fully crossed.**
- Milestone smoke: `./gradlew bootRun` boots, Flyway runs 41 migrations clean (Ctrl-C after).
- Print: "C13 feign-hc5 + test libs · WALL CLEARED · green · <SHA>".
