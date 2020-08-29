import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.0"
    kotlin("plugin.spring") version "1.4.0" apply false

    id("org.springframework.boot") version "2.3.3.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.10.RELEASE"

    id("io.gitlab.arturbosch.detekt") version "1.3.1"
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

        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    configurations.all {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR8")
        }
    }

    detekt {
        input = files(
            "src/main/kotlin",
            "src/test/kotlin"
        )
        config = files("$rootDir/detekt-config.yml")
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

//configure(subprojects - project(":common-library")) {
//    dependencies {
//        implementation(project(":common-library"))
//    }
//}
