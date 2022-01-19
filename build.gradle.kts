import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "at.ac.tuwien.trustcps"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    githubPackages("MoonlightSuite", "Moonlight")
}

dependencies {
    implementation("eu.quanticol.moonlight:core:1.0-SNAPSHOT")
    implementation("org.seleniumhq.selenium:selenium-java:4.1.1")


    implementation("org.slf4j:slf4j-simple:1.7.32")
    implementation("io.github.microutils:kotlin-logging:2.1.21")
    implementation("com.tylerthrailkill.helpers:pretty-print:2.0.2")

    // Tests
    implementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
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

fun RepositoryHandler.githubPackages(user: String, repo: String):
        MavenArtifactRepository
{
    return maven {
        url = uri("https://maven.pkg.github.com/$user/$repo")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
}
