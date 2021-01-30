import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.4.10"
}

group = "me.sttimort"
version = "0.0.1"

repositories {
//    maven("https://maven.google.com")
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.2")

    implementation("io.rsocket:rsocket-core:1.1.0")
    implementation("io.rsocket:rsocket-transport-netty:1.1.0")

    implementation("io.github.microutils:kotlin-logging:1.12.0")
    implementation("org.slf4j:slf4j-log4j12:1.7.30")
    implementation("com.google.protobuf:protobuf-java:3.14.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-Xjsr305=strict")
        allWarningsAsErrors = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}