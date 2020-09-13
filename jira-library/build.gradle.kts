dependencies {
    implementation("io.github.openfeign:feign-httpclient")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
