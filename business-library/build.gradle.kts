dependencies {
    implementation(project(":database-library"))
    implementation(project(":client-library"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
