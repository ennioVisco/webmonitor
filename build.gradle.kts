import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "at.ac.tuwien.trustcps"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to "moonlight.jar")))
    implementation(fileTree(mapOf("dir" to "libs/selenium-java-4.1.0", "include" to "*.jar")))
    implementation(fileTree(mapOf("dir" to "libs/selenium-java-4.1.0/lib", "include" to "*.jar")))
    implementation("io.github.microutils:kotlin-logging:2.1.21")
    implementation("org.slf4j:slf4j-simple:1.7.32")
    implementation("com.tylerthrailkill.helpers:pretty-print:2.0.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}