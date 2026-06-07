# C11 — THE WALL (1/3): Spring Boot 2.7 → 3.0, javax → jakarta, drop Sleuth, Cloud → 2022.0

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17. (SB 3.0 baseline = Java 17.)
4. `.draft/TODO.md`: confirm C10 is `[x]`. If not, STOP.
5. Ensure Gradle is **8.x** (`./gradlew --version`). If C10 deferred it, bump via CLI (NOT by
   editing the properties file — see _CONTEXT.md):
   `./gradlew wrapper --gradle-version 8.10.2 --distribution-type bin` — SB 3.0 needs Gradle ≥7.5.

## Scope of this task (the 3.0 jump is split across C11→C13)
- **C11 (this file):** SB 3.0 core upgrade, `javax.*`→`jakarta.*` everywhere, remove Sleuth.
- C12: Hibernate-6 type migration + Spring Security 6 rewrite.
- C13: Feign HttpClient 5 + test-lib bumps.
It is fine for the build to go red mid-wall **only if you cannot reach green** — but the GOAL is
each of C11/C12/C13 ends green. If C11 cannot compile without the C12 security change, pull the
minimal security change forward to keep C11 green and note it; do not leave a red commit.

## Change
1. `build.gradle.kts`:
   - `org.springframework.boot` → newest **3.0.x** (e.g. 3.0.13).
   - Spring Cloud BOM `2021.0.x` → **2022.0.x** (Kilburn).
   - **Remove** `implementation("org.springframework.cloud:spring-cloud-starter-sleuth")` —
     Sleuth is deleted in Spring Cloud 2022.0 and is unused in code (no tracing). Do NOT add a
     replacement.
   - `io.spring.dependency-management` → latest 1.1.x.
2. **javax → jakarta** across `src` (42 files). Replace import prefixes:
   - `javax.persistence.*` → `jakarta.persistence.*`
   - `javax.validation.*` → `jakarta.validation.*`
   - `javax.servlet.*` → `jakarta.servlet.*`
   Leave `javax.crypto.*` and `javax.sql.*` AS-IS (those are JDK packages, not Jakarta EE).
   Verify after: `grep -rho "javax\.[a-z.]*" src | sort -u` should show only `javax.crypto.`
   and `javax.sql.`.
3. SB 3.0 config property renames: check `application.yml` for `spring.redis`→`spring.data.redis`
   etc. (only if present). Flyway config keys unchanged.

## Changelog watch (Spring Boot 3.0 / Framework 6)
- Jakarta EE 9 namespace (done above). Hibernate 6 comes via SB 3.0 BOM — the
  `hibernate-types-52` lib will FAIL to compile against Hibernate 6; that's expected and fixed
  in **C12**. If it blocks compilation here, you may temporarily comment the custom `@Type`
  usages just enough to compile — but prefer doing C12's hypersistence swap immediately if the
  build can't otherwise reach green. Use judgement; end green.
- `WebSecurityConfigurerAdapter` is REMOVED in Spring Security 6 → `WebSecurityConfig.kt` will
  not compile. The full rewrite is C12; pull forward the minimal `SecurityFilterChain` bean if
  needed to compile C11.
- `antMatchers` → `requestMatchers` (Security 6).

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`
- `grep -rl "sleuth" build.gradle.kts src` → nothing.
- jakarta check (command above).

## Commit
```
git add -A
git commit -m "build!: Spring Boot 2.7->3.0, javax->jakarta, drop Sleuth

Spring Boot 3 requires Java 17 and the Jakarta EE namespace. Sleuth is
removed in Spring Cloud 2022.0 and was unused (no tracing code).

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C11 `[ ]`→`[x]` + SHA. Note anything pulled forward from C12.
- Print: "C11 SB 3.0 / jakarta / no-sleuth · green · <SHA>".
