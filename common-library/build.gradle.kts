dependencies {
    implementation("com.vladmihalcea:hibernate-types-52:2.8.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
