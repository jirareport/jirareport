val ktlint: Configuration by configurations.creating

dependencies {
    ktlint("com.pinterest.ktlint:ktlint-cli:1.8.0") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

tasks.register<JavaExec>("lintKotlin") {
    group = "verification"
    description = "Run ktlint style checks"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args(
        "--color",
        "--reporter=plain",
        "src/main/kotlin/**/*.kt",
        "src/test/kotlin/**/*.kt",
    )
}

tasks.register<JavaExec>("formatKotlin") {
    group = "formatting"
    description = "Run ktlint with autocorrect"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args(
        "--format",
        "--color",
        "--reporter=plain",
        "src/main/kotlin/**/*.kt",
        "src/test/kotlin/**/*.kt",
    )
}

tasks.named("check") {
    dependsOn("lintKotlin")
}
