plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.ktlint)
}

group = "es.unizar.webeng"
version = "2024-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.thymeleaf)
    implementation(libs.spring.boot.starter.data.jpa)

    runtimeOnly(libs.h2database.h2)

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.10")
    // Swagger documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    // Spring Boot Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    // Java JWT
    implementation("com.auth0:java-jwt:4.4.0")

    // Spring Security Test
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation(libs.spring.boot.starter.test)
    testImplementation("io.mockk:mockk:1.13.7")
    testImplementation("com.ninja-squad:springmockk:4.0.0")
    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    // Kotlin test assertions
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
}
