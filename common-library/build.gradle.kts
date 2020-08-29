dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.vladmihalcea:hibernate-types-52:2.8.0")

    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
