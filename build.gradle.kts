import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"

    id("org.springframework.boot") version "2.7.18"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"

    id("io.gitlab.arturbosch.detekt") version "1.23.6"
}

apply {
    from("gradle/tasks/CreateMigration.gradle.kts")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("com.vladmihalcea:hibernate-types-52:2.8.0")
    implementation("org.flywaydb:flyway-core")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.23")

    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")

    implementation("io.github.openfeign:feign-httpclient")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.testcontainers:postgresql:1.14.3")

    testImplementation("net.datafaker:datafaker:1.9.0")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("com.github.tomakehurst:wiremock:2.27.1")

    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("com.tngtech.archunit:archunit:0.12.0")
    
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.9")
    }
}

configurations.all {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    resolutionStrategy.force("net.java.dev.jna:jna:5.14.0", "net.java.dev.jna:jna-platform:5.14.0")
}


tasks.register<Test>("unitTest") {
    useJUnitPlatform {
        includeTags = setOf("unit")
        excludeTags = setOf("integration")
    }
}

detekt {
    source.setFrom(files("src/main/kotlin", "src/test/kotlin"))
    config.setFrom(files("$projectDir/detekt-config.yml"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "17"
    }
}
