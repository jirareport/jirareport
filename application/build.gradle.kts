dependencies {
    api(project(":business-library"))

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

    testImplementation(project(":database-library"))
}

tasks.bootJar {
    enabled = true
}
