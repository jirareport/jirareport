import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.20"
    kotlin("plugin.spring") version "2.3.20"
    kotlin("plugin.jpa") version "2.3.20"

    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
}

apply {
    from("gradle/tasks/CreateMigration.gradle.kts")
    from("gradle/tasks/Ktlint.gradle.kts")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:4.0.6"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("io.hypersistence:hypersistence-utils-hibernate-71:3.15.3")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.flywaydb:flyway-database-postgresql")

    implementation("tools.jackson.module:jackson-module-kotlin:3.1.2")
    implementation("tools.jackson.dataformat:jackson-dataformat-xml:3.1.2")
    // hypersistence-utils + rest-assured still use Jackson 2 (databind 2.21.x). Provide its
    // JavaTime + Kotlin modules so JSON columns (java.time, Kotlin data classes) (de)serialize.
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.21.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.21.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.3.20")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.3.20")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.testcontainers:postgresql:1.20.6")

    testImplementation("net.datafaker:datafaker:1.9.0")
    testImplementation("io.rest-assured:rest-assured:5.5.0")
    testImplementation("org.wiremock:wiremock-standalone:3.9.0")

    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("com.tngtech.archunit:archunit:1.3.0")

}

configurations.all {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    resolutionStrategy.force("net.java.dev.jna:jna:5.14.0", "net.java.dev.jna:jna-platform:5.14.0")
    // Spring Boot 4 ships Groovy 5.0.x, which rest-assured does not support yet
    // (NPE in Groovy ClosureMetaClass). Pin Groovy back to 4.0.x for the test classpath.
    resolutionStrategy.eachDependency {
        if (requested.group == "org.apache.groovy") {
            useVersion("4.0.28")
            because("rest-assured is incompatible with Groovy 5 (Spring Boot 4 default)")
        }
    }
}


tasks.register<Test>("unitTest") {
    useJUnitPlatform {
        includeTags = setOf("unit")
        excludeTags = setOf("integration")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()

    // Testcontainers / docker-java compatibility, all overridable by the environment so CI and
    // non-Colima setups are unaffected — these are local fallbacks only:
    // docker-java defaults to Docker API 1.32, which newer engines (Docker 29+/Colima) reject.
    systemProperty("api.version", System.getenv("DOCKER_API_VERSION") ?: "1.45")
    // Only pin the local Docker socket when the environment hasn't already set DOCKER_HOST
    // (e.g. CI / remote Docker) and the default socket actually exists on this machine.
    if (System.getenv("DOCKER_HOST") == null && file("/var/run/docker.sock").exists()) {
        environment("DOCKER_HOST", "unix:///var/run/docker.sock")
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "24"
    targetCompatibility = "24"
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget.set(JvmTarget.JVM_24)
    }
}
