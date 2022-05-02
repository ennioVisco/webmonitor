import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "at.ac.tuwien.trustcps"
version = "1.0-SNAPSHOT"

plugins {
    application
    jacoco
    id("org.sonarqube") version "3.3"
    kotlin("jvm") version "1.6.20"
    id("org.openjfx.javafxplugin") version "0.0.11"
    id("org.javamodularity.moduleplugin") version ("1.8.10") apply false
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

javafx {
    version = "17"
    modules = listOf("javafx.base", "javafx.controls", "javafx.swing")
}

dependencies {
    // Configuration files
    implementation(kotlin("script-runtime"))
    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.6.21")

    // Moonlight
    //implementation("eu.quanticol.moonlight:core:1.0-SNAPSHOT")
    implementation(files("lib/moonlight.jar"))

    // Selenium
    implementation("org.seleniumhq.selenium:selenium-java:4.1.3")
    implementation("io.github.bonigarcia:webdrivermanager:5.1.1")

    // Charts
    implementation("eu.hansolo.fx:charts:17.1.7")

    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.apache.logging.log4j:log4j-api:2.17.2")
    implementation("org.apache.logging.log4j:log4j-core:2.17.2")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.2")
//    implementation("ch.qos.logback:logback-classic:1.2.10")

    implementation("com.tylerthrailkill.helpers:pretty-print:2.0.2")

    // Tests
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("com.github.stefanbirkner:system-lambda:1.2.1")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run

}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required.set(true)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    //executableDir = "$buildDir/../src/main/kotlin/at/ac/tuwien/trustcps"
    println("Current exec dir: ${executableDir}")
    fun pkg(name: String) = "${group}.${name}Kt"
    mainClass.set(pkg("Main"))
    //println("Current sources dir: ${buildDir}")
}

fun RepositoryHandler.githubPackages(user: String, repo: String):
        MavenArtifactRepository {
    return maven {
        url = uri("https://maven.pkg.github.com/$user/$repo")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USER")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
