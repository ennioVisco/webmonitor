import org.jetbrains.kotlin.gradle.tasks.*

group = "at.ac.tuwien.trustcps"
version = "1.0-SNAPSHOT"

plugins {
    application
    jacoco
    checkstyle
    id("org.sonarqube") version "4.0.0.2929"
    kotlin("jvm") version "1.8.10"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.javamodularity.moduleplugin") version ("1.8.12") apply false
    id("org.jetbrains.dokka") version "1.8.10"
}

repositories {
    mavenCentral()
    //githubPackages("MoonlightSuite", "Moonlight")
}

sonar {
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
    modules =
        listOf(
            "javafx.base",
            "javafx.controls",
            "javafx.swing",
            "javafx.web"
        )
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
    implementation("org.seleniumhq.selenium:selenium-java:4.8.1")
    implementation("io.github.bonigarcia:webdrivermanager:5.3.2")

    // TestFX (headless GUI)
    implementation("org.testfx:testfx-core:4.0.16-alpha")
    implementation("org.testfx", "testfx-junit5", "4.0.16-alpha")
    implementation("org.testfx:openjfx-monocle:jdk-12.0.1+2")

    // Dokka
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.8.10")

    // Charts
    implementation("eu.hansolo.fx:charts:17.1.27")

    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    runtimeOnly("org.slf4j:slf4j-api:2.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.6")

    // Pretty printing (debug)
    implementation("com.tylerthrailkill.helpers:pretty-print:2.0.2")

    // Tests
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("com.github.stefanbirkner:system-lambda:1.2.1")
}

fun runtimeArgs(exec: Any) {
    val arguments = listOf(
        "--enable-preview",
        "--add-exports", "javafx.graphics/com.sun.glass.ui=ALL-UNNAMED",
        "--add-exports", "javafx.graphics/com.sun.glass.utils=ALL-UNNAMED",
        "--add-opens", "javafx.graphics/com.sun.glass.ui=ALL-UNNAMED",
        "--add-opens", "javafx.graphics/com.sun.glass.utils=ALL-UNNAMED"
    )
    when (exec) {
        is JavaExec -> exec.jvmArgs(arguments)
        is Test -> exec.jvmArgs(arguments)
        else -> throw IllegalArgumentException("Unknown exec type: $exec")
    }
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("--enable-preview")
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

val headlessJavaFXSettings = mapOf(
    //"java.awt.headless" to true,
    "testfx.robot" to "glass",
    "testfx.headless" to true,
    "glass.platform" to "Monocle",
    "monocle.platform" to "Headless",
    "headless.geometry" to "1920x1080-32",
    //"prism.order" to "sw"
)

tasks.withType<JavaExec> {
    runtimeArgs(this)
    systemProperties = headlessJavaFXSettings
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
