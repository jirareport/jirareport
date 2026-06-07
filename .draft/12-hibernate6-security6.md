# C12 — THE WALL (2/3): Hibernate 6 types + Spring Security 6 rewrite

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 17` ; `java -version` → 17.
4. `.draft/TODO.md`: confirm C11 is `[x]`. If not, STOP.
   (If C11 already pulled some of this forward to stay green, finish the remainder here.)

## Part A — hibernate-types-52 → hypersistence-utils (Hibernate 6)
`com.vladmihalcea:hibernate-types-52` does not work with Hibernate 6 (bundled by SB 3.0). The
maintained successor is `io.hypersistence:hypersistence-utils-hibernate-63` (match the Hibernate
6.x minor that SB 3.0 ships; if SB ships Hibernate 6.1, use `-hibernate-62`/`-63` per the
hypersistence compatibility table — pick the one matching the resolved Hibernate version).

1. `build.gradle.kts`: replace
   `implementation("com.vladmihalcea:hibernate-types-52:2.8.0")`
   with `implementation("io.hypersistence:hypersistence-utils-hibernate-6x:<latest>")`.
2. Entities using the old types (run `grep -rl "vladmihalcea\|@Type\|TypeDef" src`):
   - `domain/entity/IssueEntity.kt`, `BoardEntity.kt`, `IssuePeriodEntity.kt`, `BaseEntity.kt`.
   Migrate:
   - package `com.vladmihalcea.hibernate.type.*` → `io.hypersistence.utils.hibernate.type.*`.
   - Hibernate 6 changed type-mapping: replace `@TypeDef`/`@Type(type = "...")` with the
     Hibernate 6 style `@Type(JsonType::class)` (annotation now takes the class, not a string),
     or `@JdbcTypeCode(SqlTypes.JSON)` for JSON columns. Follow the hypersistence-utils
     Hibernate-6 README for JSON/array columns used here.
3. `src/main/resources/hibernate-types.properties` (`hibernate.types.print.banner=false`):
   rename to the hypersistence key if the banner property changed, or delete if no longer
   recognized. Check the lib docs.

## Part B — Spring Security 6 (`security/WebSecurityConfig.kt`)
`WebSecurityConfigurerAdapter` is removed. Rewrite as a component-based config exposing a
`SecurityFilterChain` bean. The existing config (for reference) does: permit
`GET /actuator/**` and `OPTIONS /**`, authenticate everything else, STATELESS sessions, a
custom formLogin success/failure handler setting `X-Auth-Token` / `X-Auth-Fail-Reason`, a
custom auth entry point returning 401, anonymous disabled, httpBasic disabled, plus two custom
`AuthenticationProvider`s (`JiraAuthenticationProvider`, `TokenAuthenticationProvider`) and a
`CorsConfiguration` bean. Preserve ALL of this behavior.

Rewrite shape (adapt; use the lambda DSL):
```kotlin
@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val jiraAuthenticationProvider: JiraAuthenticationProvider,
    private val tokenAuthenticationProvider: TokenAuthenticationProvider,
    private val corsConfiguration: CorsConfiguration,
) {
    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager =
        http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .authenticationProvider(jiraAuthenticationProvider)
            .authenticationProvider(tokenAuthenticationProvider)
            .build()

    @Bean
    fun filterChain(http: HttpSecurity, authManager: AuthenticationManager): SecurityFilterChain {
        http
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                it.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                it.anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authenticationManager(authManager)
            .formLogin { /* same success/failure handlers, jakarta.servlet HttpServletResponse */ }
            .exceptionHandling { /* 401 entry point */ }
            .anonymous { it.disable() }
            .httpBasic { it.disable() }
            .csrf { it.disable() }   // STATELESS API
        return http.build()
    }
}
```
- `antMatchers` → `requestMatchers`.
- `javax.servlet.http.HttpServletResponse` → `jakarta.servlet.http.HttpServletResponse`.
- If the old code referenced `WebSecurity` `configure(web)` (e.g. ignoring paths), port it to a
  `WebSecurityCustomizer` bean. Check the FULL original file before deleting it.

## Verify
- `docker ps` ; `./gradlew clean build` ; `./gradlew detekt`
- Integration tests covering auth (the `SpringBootTest` IT files) must pass — they prove the
  security rewrite preserved behavior.
- `grep -rl "vladmihalcea\|WebSecurityConfigurerAdapter" src` → nothing.

## Commit
```
git add -A
git commit -m "build!: migrate to Hibernate 6 types (hypersistence-utils) and Spring Security 6

WebSecurityConfigurerAdapter removed in Security 6 -> SecurityFilterChain
bean. hibernate-types-52 replaced by hypersistence-utils for Hibernate 6.

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C12 `[ ]`→`[x]` + SHA.
- Print: "C12 Hibernate6 + Security6 · green · <SHA>".
