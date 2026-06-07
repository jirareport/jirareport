# Upgrade progress — controller / single source of truth

Rules:
- A run without a specific file picks the **first unchecked** row below.
- Before starting a row, confirm the previous row is `[x]`. If not, STOP.
- On green build + commit: flip `[ ]`→`[x]` and append the commit SHA at end of the row.
- Never check a row whose `./gradlew clean build` was not green.

| done | id | task file | JDK | moves |
|------|----|-----------|-----|-------|
- [x] C0  `00-baseline-jcenter-jvmtarget.md`  (JDK 11) — drop jcenter, jvmTarget 13→11 — 5f5eb5b
- [x] C1  `01-datafaker.md`                    (JDK 11) — javafaker→datafaker (tests) — aba6f13
- [x] C2  `02-kotlin-1.5.md`                   (JDK 11) — Kotlin 1.4→1.5 — db63722
- [x] C3  `03-kotlin-1.6.md`                   (JDK 11) — Kotlin 1.5→1.6 — fe58a9a
- [x] C4  `04-spring-2.4.md`                   (JDK 11) — SB 2.3→2.4, Cloud Hoxton→2020.0 — d7a10f0
- [x] C5  `05-spring-2.5-gradle7-jdk17.md`     (JDK 17) — SB 2.4→2.5, Gradle→7.6, JDK→17, jvmTarget→17 — b04421e
- [ ] C6  `06-spring-2.6.md`                   (JDK 17) — SB 2.5→2.6, Cloud 2020.0→2021.0
- [ ] C7  `07-spring-2.7.md`                   (JDK 17) — SB 2.6→2.7
- [ ] C8  `08-kotlin-1.7.md`                   (JDK 17) — Kotlin 1.6→1.7
- [ ] C9  `09-kotlin-1.8.md`                   (JDK 17) — Kotlin 1.7→1.8
- [ ] C10 `10-kotlin-1.9-gradle8.md`           (JDK 17) — Kotlin 1.8→1.9, Gradle 7.6→8.x
- [ ] C11 `11-spring-3.0-jakarta.md`           (JDK 17) — SB 2.7→3.0, javax→jakarta, drop sleuth, Cloud→2022.0
- [ ] C12 `12-hibernate6-security6.md`         (JDK 17) — hypersistence-utils, Spring Security 6 rewrite
- [ ] C13 `13-feign-hc5-testlibs.md`           (JDK 17) — feign-hc5, wiremock 3.x, test libs
- [ ] C14 `14-spring-3.1.md`                   (JDK 17) — SB 3.0→3.1
- [ ] C15 `15-spring-3.2.md`                   (JDK 17) — SB 3.1→3.2, Cloud 2022.0→2023.0
- [ ] C16 `16-kotlin-2.0-k2.md`                (JDK 17) — Kotlin 1.9→2.0 (K2), jvm-default flag, detekt
- [ ] C17 `17-spring-3.3.md`                   (JDK 17) — SB 3.2→3.3
- [ ] C18 `18-spring-3.4.md`                   (JDK 17) — SB 3.3→3.4, Cloud 2023.0→2024.0
- [ ] C19 `19-kotlin-2.1.md`                   (JDK 17) — Kotlin 2.0→2.1
- [ ] C20 `20-spring-3.5-gradle9-jdk21.md`     (JDK 21) — SB 3.4→3.5, Cloud→2025.0, Gradle→9.x, JDK→21
- [ ] C21 `21-kotlin-2.2.md`                   (JDK 21) — Kotlin 2.1→2.2
- [ ] C22 `22-spring-4.0-jdk25.md`             (JDK 25) — SB 3.5→4.0.6, Cloud→2026.0, Gradle→9.5.1, JDK→25, Dockerfile, detekt 2.0-alpha
- [ ] C23 `23-kotlin-2.3.md`                   (JDK 25) — Kotlin 2.2→2.3.20

## End-state pass criteria (verify after C23)
- `build.gradle.kts`: Kotlin 2.3.20, Spring Boot 4.0.6, Spring Cloud 2026.0, detekt 2.0.0-alpha.
- `gradle-wrapper.properties`: `gradle-9.5.1`.
- `grep -rl "javax\." src` → nothing.
- `grep -ri "sleuth\|javafaker\|vladmihalcea\|jcenter" build.gradle.kts src` → nothing.
- `cli-assistant env java use 25 && ./gradlew clean build` → green (Docker running).
- `docker build -t jirareport:upgrade .` → succeeds.
