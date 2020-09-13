dependencies {
    implementation(project(":business-library"))
    implementation(project(":jira-library"))

    implementation("org.springframework.data:spring-data-commons")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation(project(":database-library"))
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks.bootJar {
    enabled = true
}
