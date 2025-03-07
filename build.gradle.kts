import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val javaTarget = JavaVersion.VERSION_17

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.ksp)
    alias(libs.plugins.telegram.bot)
}

group = "com.example.springbot"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = javaTarget

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    
    // Добавляем поддержку PostgreSQL
    implementation("org.postgresql:postgresql")
    
    // Добавляем H2 для локальной разработки, если PostgreSQL недоступен
    runtimeOnly("com.h2database:h2")

    implementation("eu.vendeli:telegram-bot-jvm:7.9.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

kotlin {
    jvmToolchain(javaTarget.majorVersion.toInt())
}

tasks {
    compileJava {
        targetCompatibility = javaTarget.majorVersion
        sourceCompatibility = javaTarget.majorVersion
    }

    withType<Test> {
        useJUnitPlatform()
    }
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = javaTarget.majorVersion
        }
    }
}