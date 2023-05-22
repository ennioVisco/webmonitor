import com.vanniktech.maven.publish.*
import org.jetbrains.kotlin.gradle.tasks.*

group = providers.gradleProperty("project.group").get()
//version = "0.1.0-SNAPSHOT"

val ENABLE_PREVIEW = "--enable-preview"
val GARBAGE_COLLECTOR = "-XX:+UseParallelGC"

val PROJECT_VERSION = try {
    val v = providers.gradleProperty("projectVersion").get()
    println("Project version: $v")
    v
} catch (e: Exception) {
    println("ERROR - Unable to find version: ${e.message}")
    "0.1.0-SNAPSHOT"
}

plugins {
    // Environment
    id("me.filippov.gradle.jvm.wrapper") version "0.14.0"
    kotlin("jvm") version "1.8.20"

    // GUI
    id("org.openjfx.javafxplugin") version "0.0.13"

    // Code quality, testing & documentation
    jacoco
    checkstyle
    id("org.sonarqube") version "4.0.0.2929"
    id("org.jetbrains.dokka") version "1.8.10"

    // Modularization & packaging
    application
    id("org.javamodularity.moduleplugin") version ("1.8.12") apply false
    id("org.panteleyev.jpackageplugin") version "1.5.2"

    // Releases & publishing
    id("it.nicolasfarabegoli.conventional-commits") version "3.1.1"
    id("com.vanniktech.maven.publish") version "0.25.2"
//    signing
}

repositories {
    mavenCentral()
}

java {
    withJavadocJar()
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

//publishing {
//    publications {
//        create<MavenPublication>("mavenJava") {
//            groupId = providers.gradleProperty("project.group").get()
//            artifactId = providers.gradleProperty("project.name").get()
//            version = PROJECT_VERSION
//            from(components["java"])
//            versionMapping {
//                usage("java-api") {
//                    fromResolutionOf("runtimeClasspath")
//                }
//                usage("java-runtime") {
//                    fromResolutionResult()
//                }
//            }
//            pom {
//                name.set(rootProject.name)
//                description.set("A formal approach to monitoring web pages as spatio-temporal traces.")
//                url.set("https://enniovisco.github.io/webmonitor/")
//                licenses {
//                    license {
//                        name.set("MIT License")
//                        url.set("https://raw.githubusercontent.com/ennioVisco/webmonitor/master/LICENSE")
//                    }
//                }
//                developers {
//                    developer {
//                        id.set("ennioVisco")
//                        name.set("Ennio Visconti")
//                        email.set("ennio.visconti@gmail.com")
//                    }
//                }
//                scm {
//                    connection.set("scm:git:git://github.com/enniovisco/webmonitor.git")
//                    developerConnection.set("scm:git:ssh://github.com/enniovisco/webmonitor.git")
//                    url.set("http://github.com/enniovisco/webmonitor")
//                }
//            }
//        }
//    }
//    repositories {
//        maven {
//            name = "sonaType"
//
//            credentials(PasswordCredentials::class)
//
//            val releasesRepoUrl =
//                uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
//            val snapshotsRepoUrl =
//                uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
//
//            url = if (PROJECT_VERSION.endsWith("SNAPSHOT"))
//                snapshotsRepoUrl else releasesRepoUrl
//        }
//    }
//}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01, automaticRelease = true)

    signAllPublications()

    pom {
        name.set(rootProject.name)
        description.set("A formal approach to monitoring web pages as spatio-temporal traces.")
        url.set("https://enniovisco.github.io/webmonitor/")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://raw.githubusercontent.com/ennioVisco/webmonitor/master/LICENSE")
            }
        }
        developers {
            developer {
                id.set("ennioVisco")
                name.set("Ennio Visconti")
                email.set("ennio.visconti@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:git://github.com/enniovisco/webmonitor.git")
            developerConnection.set("scm:git:ssh://github.com/enniovisco/webmonitor.git")
            url.set("http://github.com/enniovisco/webmonitor")
        }
    }
}

//signing {
////    val signingKeyId: String? by project
//    val signingKey: String? by project
//    val signingPassword: String? by project
////    useInMemoryPgpKeys(signingKey, signingPassword)
//    sign(publishing.publications["mavenJava"])
//}


sonar {
    properties {
        property("sonar.projectKey", "ennioVisco_${rootProject.name}")
        property("sonar.organization", "enniovisco")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

javafx {
    version = "17"
    modules =
        listOf("javafx.base", "javafx.controls", "javafx.swing", "javafx.web")
}

dependencies {
    // Configuration files
    implementation(kotlin("script-runtime"))
    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.8.20")

    // Moonlight
    //implementation("eu.quanticol.moonlight:core:1.0-SNAPSHOT")
    implementation(files("lib/moonlight.jar"))

    // Selenium
    implementation("org.seleniumhq.selenium:selenium-java:4.8.3")
    implementation("org.seleniumhq.selenium:selenium-http-jdk-client:4.8.3")

    implementation("io.github.bonigarcia:webdrivermanager:5.3.2")

    // TestFX (headless GUI)
    implementation("org.testfx:testfx-core:4.0.16-alpha")
    implementation("org.testfx:testfx-junit5:4.0.16-alpha")
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
    testImplementation(kotlin("reflect"))
}

fun runtimeArgs(exec: Any) {
    val arguments = listOf(
        GARBAGE_COLLECTOR,
        ENABLE_PREVIEW,
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

tasks tasks@{
    withType<JavaCompile> {
        options.compilerArgs.add(ENABLE_PREVIEW)
    }

    dokkaHtml.configure {
        outputDirectory.set(buildDir.resolve("dokka"))
    }

    test {
        useJUnitPlatform()
        runtimeArgs(this)
        finalizedBy(this@tasks.jacocoTestReport) // report is always generated after tests run
    }

    withType<JavaExec> {
        val headlessJavaFXSettings = mapOf(
            //"java.awt.headless" to true,
            "testfx.robot" to "glass",
            "testfx.headless" to true,
            "glass.platform" to "Monocle",
            "monocle.platform" to "Headless",
            "headless.geometry" to "1920x1080-32",
            //"prism.order" to "sw"
        )

        runtimeArgs(this)
        systemProperties = headlessJavaFXSettings
        System.setProperty("webdriver.http.factory", "jdk-http-client")
    }

    jacocoTestReport {
        dependsOn(this@tasks.test) // tests are required to run before generating the report
        reports {
            xml.required.set(true)
        }
    }

    named("publishAllPublicationsToMavenCentralRepository") {
        dependsOn("kotlinSourcesJar")
    }

    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

fun pkg(name: String) = "${group}.${name}Kt"
application {
    applicationDefaultJvmArgs = listOf(ENABLE_PREVIEW)
    println("Current exec dir: $executableDir")
    mainClass.set(pkg("Main"))
}

task("copyDependencies", Copy::class) {
    from(configurations.runtimeClasspath).into("$buildDir/jars")
}

task("copyJar", Copy::class) {
    from(tasks.jar).into("$buildDir/jars")
}


tasks.jpackage {
    dependsOn("build", "copyDependencies", "copyJar")

    // App Info
    appName = "WebMonitor"
    vendor = "enniovisco.com"
    appVersion = PROJECT_VERSION
    copyright = "Copyright (c) 2023 Vendor"

    // App settings (non-modular)
    mainJar = tasks.jar.get().archiveFileName.get()
    mainClass = pkg("Main")
    input = "$buildDir/jars"

    // App settings (modular)
//    runtimeImage = System.getProperty("java.home")
//    module = "org.app.module/org.app.MainClass"
//    modulePaths = listOf(File("$buildDir/jmods").toString())

    // Build destination
    destination = "$buildDir/dist"

    // Java Options
    javaOptions = listOf("-Dfile.encoding=UTF-8")

    windows {
        winMenu = true
        winDirChooser = true
        winConsole = true
    }
}
