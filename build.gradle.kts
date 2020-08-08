import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"

    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"

    id("io.gitlab.arturbosch.detekt") version "1.3.1"
}

apply {
    from("gradle/tasks/CreateMigration.gradle.kts")
}

java.sourceCompatibility = JavaVersion.VERSION_12
java.targetCompatibility = JavaVersion.VERSION_12

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("com.vladmihalcea:hibernate-types-52:2.8.0")
    implementation("org.flywaydb:flyway-core")

    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")

    implementation("io.github.openfeign:feign-httpclient")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.testcontainers:postgresql:1.14.3")

    testImplementation("com.github.javafaker:javafaker:1.0.1")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("com.github.tomakehurst:wiremock:2.27.1")

    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.10.0")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.3.1")

    testImplementation("com.tngtech.archunit:archunit:0.12.0")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.RELEASE")
    }
}

configurations.all {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
        jvmTarget = "12"
    }
}

detekt {
    input = files(
        "src/main/kotlin",
        "src/test/kotlin"
    )
    config = files("$projectDir/detekt-config.yml")
}

tasks.register<Test>("unitTest") {
    useJUnitPlatform {
        includeTags = setOf("unit")
        excludeTags = setOf("integration")
    }
}
