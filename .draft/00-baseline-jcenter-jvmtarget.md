# C0 — Toolchain floor: drop jcenter, lower jvmTarget, establish green baseline

## Before you start
1. Read `.draft/_CONTEXT.md` in full.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java install 11` (if missing) then `cli-assistant env java use 11` ; `java -version` → must show 11.
4. This is the FIRST task; `.draft/TODO.md` C0 should be unchecked.

## Why (VERIFIED against the real repo — three coupled blockers)
The pristine current state cannot build green on any JDK available here. Confirmed by running it:
1. **`jvmTarget = 13` vs JDK 11**: tests compile to class-file v57 (Java 13) and fail at runtime
   on JDK 11 (max v55) with `UnsupportedClassVersionError`. JDK 13/14/15 are NOT installable
   (`cli-assistant env java available` jumps 11→17) and Gradle 6.6.1 cannot run on JDK 17+. So
   the only fix is to lower `jvmTarget` to 11.
2. **`jcenter()` is dead** (bintray sunset 2021) — it 404s.
3. **detekt 1.14.2 is unresolvable**: it hard-pins the transitive `org.jetbrains.kotlinx:
   kotlinx-html-jvm:0.7.2`, which was **bintray-only and is 404 on Maven Central** (earliest on
   Central is 0.7.3). Removing jcenter therefore makes detekt fail to resolve entirely. We must
   redirect that one transitive to 0.7.3 (present on Central).

We are NOT bumping Gradle yet (SB 2.3.5's Gradle plugin predates Gradle 7).

## Change (this commit only)
In `build.gradle.kts`:
- In `repositories { ... }` delete the `jcenter()` line. Keep `mavenCentral()`.
- In the `tasks.withType<KotlinCompile>` block change `jvmTarget = "13"` → `jvmTarget = "11"`.
- **Fix detekt's dead transitive** by forcing `kotlinx-html-jvm` to 0.7.3 on the detekt
  configuration (keeps detekt 1.14.2 itself unchanged):
  ```kotlin
  configurations.named("detekt") {
      resolutionStrategy.force("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
  }
  ```
  (Equivalent alternative if the force is awkward: bump the detekt plugin + `detekt-formatting`
  from 1.14.2 to the newest 1.x that still supports Kotlin 1.4 and references kotlinx-html 0.7.3,
  e.g. 1.17.1. Prefer the minimal `force` to keep this commit purely "make it build".)

Do not touch Kotlin, Spring, Gradle, or application dependency versions.

## Verify (must be green)
- `docker ps` (start Colima if it errors: `colima start`)
- `./gradlew clean build`   # compile + unit + arch + integration
- `./gradlew detekt`
If integration tests cannot reach Postgres, ensure Docker is up; do not skip them.

## Commit
```
git add -A
git commit -m "build: drop dead jcenter repo and set jvmTarget 13->11 for JDK 11 baseline

Establishes a green baseline buildable on JDK 11 before the staged
Kotlin/Spring upgrade. jcenter shut down in 2021.

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: flip C0 `[ ]`→`[x]`, append the commit SHA.
- Print: "C0 baseline green on JDK 11 · build+detekt pass · <SHA>".
