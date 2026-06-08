import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    kotlin("plugin.jpa") version "2.2.21"

    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"

    // detekt 1.23.8 does not support Kotlin 2.1; deferred to C22 (2.0.0-alpha)
    // id("io.gitlab.arturbosch.detekt") version "1.23.8"
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
    implementation("io.hypersistence:hypersistence-utils-hibernate-60:3.9.0")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.2.21")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.2.21")

    implementation("io.github.openfeign:feign-hc5")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.testcontainers:postgresql:1.20.6")

    testImplementation("net.datafaker:datafaker:1.9.0")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.wiremock:wiremock-standalone:3.9.0")

    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("com.tngtech.archunit:archunit:1.3.0")

    // detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
    }
}

extra["jackson-bom.version"] = "2.15.4"

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

// detekt { ... }  // deferred to C22

tasks.withType<Test> {
    useJUnitPlatform()
    environment("DOCKER_HOST", "unix:///var/run/docker.sock")
    jvmArgs("-Dapi.version=1.45")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget.set(JvmTarget.JVM_21)
    }
}
