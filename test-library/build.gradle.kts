dependencies {
    implementation(project(":business-library"))
    implementation(project(":database-library"))

    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework:spring-tx")

    api("org.springframework.boot:spring-boot-starter-test")

    api("org.testcontainers:postgresql:1.14.3")

    api("com.github.javafaker:javafaker:1.0.1")
    api("io.rest-assured:rest-assured")
    api("com.github.tomakehurst:wiremock:2.27.1")

    api("org.springframework.security:spring-security-test")
    api("org.springframework.boot:spring-boot-starter-test")
    api("io.mockk:mockk:1.10.0")

    api("com.tngtech.archunit:archunit:0.12.0")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
