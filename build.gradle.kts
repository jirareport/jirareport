import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.0"
    kotlin("plugin.spring") version "1.4.0" apply false
    kotlin("plugin.jpa") version "1.4.0" apply false

    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"

    id("io.gitlab.arturbosch.detekt") version "1.12.0"
}

apply {
    from("$rootDir/gradle/tasks/CreateMigration.gradle.kts")
}

repositories {
    mavenCentral()
    jcenter()
}

allprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
            jvmTarget = "13"
        }
    }
}

subprojects {
    apply {
        plugin("java-library")

        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.jetbrains.kotlin.plugin.jpa")

        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")

        plugin("io.gitlab.arturbosch.detekt")
    }

    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        implementation("org.springframework.cloud:spring-cloud-starter-sleuth")

        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.12.0")
    }

    configurations.all {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR8")
            mavenBom("org.springframework.data:spring-data-boom:${dependencyManagement.importedProperties["spring-data-releasetrain.version"]}")
        }
    }

    detekt {
        input = files(
            "src/main/kotlin",
            "src/test/kotlin"
        )
        config = files("$rootDir/detekt-config.yml")
    }

    tasks.detekt {
        doLast {
            delete("$rootDir/config")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.register<Test>("unitTest") {
        useJUnitPlatform {
            includeTags = setOf("unit")
            excludeTags = setOf("integration")
        }
    }

}

configure(subprojects - project(":common-library")) {
    dependencies {
        implementation(project(":common-library"))
    }
}

configure(subprojects - project(":test-library")) {
    dependencies {
        testImplementation(project(":test-library"))
    }
}

tasks.bootJar {
    enabled = false
}

