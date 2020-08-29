dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.postgresql:postgresql")
    implementation("com.vladmihalcea:hibernate-types-52:2.8.0")
    implementation("org.flywaydb:flyway-core")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
