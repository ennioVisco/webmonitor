plugins {
    kotlin("jvm")
}

group = "com.enniovisco"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-scripting-common")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    implementation(project(":script-definition")) // the script definition module
}

tasks.test {
    useJUnitPlatform()
}
