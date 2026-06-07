# C1 — Replace abandoned javafaker with datafaker (test-only)

## Before you start
1. Read `.draft/_CONTEXT.md`.
2. `cd /Users/leoferreira/workspace/jirareport`
3. `cli-assistant env java use 11` ; `java -version` → 11.
4. `.draft/TODO.md`: confirm C0 is `[x]`. If not, STOP.

## Why
`com.github.javafaker:javafaker` is abandoned and blocks newer JVMs. `net.datafaker:datafaker`
is the maintained drop-in successor. This is a self-contained, test-only change done early to
de-risk later JDK bumps. The API is nearly identical; the package changes
`com.github.javafaker` → `net.datafaker`.

## Change (this commit only)
1. In `build.gradle.kts` replace:
   `testImplementation("com.github.javafaker:javafaker:1.0.1")`
   with the latest datafaker, e.g. `testImplementation("net.datafaker:datafaker:<latest>")`
   (resolve newest stable on mvnrepository; 2.x line).
2. Update imports `com.github.javafaker.*` → `net.datafaker.*` in these test files (the `Faker`
   class lives at `net.datafaker.Faker`):
   - `src/test/.../service/DueDateServiceTest.kt`
   - `src/test/.../testlibrary/FakerConfig.kt`
   - `src/test/.../testlibrary/extension/faker/Jira.kt`
   - `src/test/.../testlibrary/extension/faker/FakerExtension.kt`
   - all files under `src/test/.../testlibrary/factory/domain/**` that import javafaker
   (run `grep -rl "javafaker" src` to get the exact current list).
3. Fix any API differences datafaker introduces (most `faker.x().y()` calls are unchanged;
   a few providers were renamed — follow compiler errors).

## Verify
- `docker ps` (Colima up)
- `./gradlew clean build`
- `./gradlew detekt`
- `grep -rl "javafaker" src` → nothing.

## Commit
```
git add -A
git commit -m "test: migrate javafaker -> datafaker

javafaker is abandoned; datafaker is the maintained successor and works
on modern JDKs. Test-only change.

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

## Record
- `.draft/TODO.md`: C1 `[ ]`→`[x]` + SHA.
- Print: "C1 datafaker · build+detekt green · <SHA>".
