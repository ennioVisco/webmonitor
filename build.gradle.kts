import org.jetbrains.kotlin.gradle.tasks.*

group = "at.ac.tuwien.trustcps"
version = "1.0-SNAPSHOT"

plugins {
    application
    jacoco
    id("org.sonarqube") version "3.3"
    kotlin("jvm") version "1.6.20"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.javamodularity.moduleplugin") version ("1.8.10") apply false
    id("org.jetbrains.dokka") version "1.6.21"
}

repositories {
    mavenCentral()
    //githubPackages("MoonlightSuite", "Moonlight")
}

sonarqube {
    properties {
        property("sonar.projectKey", "ennioVisco_webmonitor")
        property("sonar.organization", "enniovisco")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}

javafx {
    version = "17"
    modules = listOf("javafx.base", "javafx.controls", "javafx.swing")
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
}

dependencies {
    // Configuration files
    implementation(kotlin("script-runtime"))
    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.6.21")

    // Moonlight
    //implementation("eu.quanticol.moonlight:core:1.0-SNAPSHOT")
    implementation(files("lib/moonlight.jar"))

    // Selenium
    implementation("org.seleniumhq.selenium:selenium-java:4.3.0")
    implementation("io.github.bonigarcia:webdrivermanager:5.2.3")

    // Dokka
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.7.10")


    // Charts
    implementation("eu.hansolo.fx:charts:17.1.13")

    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
    runtimeOnly("org.slf4j:slf4j-api:1.7.36")
    implementation("ch.qos.logback:logback-classic:1.2.11")

    implementation("com.tylerthrailkill.helpers:pretty-print:2.0.2")

    // Tests
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("io.mockk:mockk:1.12.5")
    testImplementation("com.github.stefanbirkner:system-lambda:1.2.1")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("--enable-preview")
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.withType<JavaExec>() {
    jvmArgs("--enable-preview")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required.set(true)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    //executableDir = "$buildDir/../src/main/kotlin/at/ac/tuwien/trustcps"
    println("Current exec dir: $executableDir")
    fun pkg(name: String) = "${group}.${name}Kt"
    mainClass.set(pkg("Main"))
    //println("Current sources dir: ${buildDir}")
}

fun RepositoryHandler.githubPackages(user: String, repo: String):
        MavenArtifactRepository {
    return maven {
        url = uri("https://maven.pkg.github.com/$user/$repo")
        credentials {
            username = project.findProperty("gpr.user") as String?
                ?: System.getenv("GITHUB_USER")
            password = project.findProperty("gpr.key") as String?
                ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
