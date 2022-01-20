import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "at.ac.tuwien.trustcps"
version = "1.0-SNAPSHOT"

plugins {
    application
    jacoco
    id("org.sonarqube") version "3.3"
    kotlin("jvm") version "1.6.10"
}



repositories {
    mavenCentral()
    githubPackages("MoonlightSuite", "Moonlight")
}

sonarqube {
    properties {
        property("sonar.projectKey", "ennioVisco_webmonitor")
        property("sonar.organization", "enniovisco")
        property("sonar.host.url", "https://sonarcloud.io")
    }
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
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run

}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
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
