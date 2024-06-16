plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = providers.gradleProperty("project.name").get()
include("script-definition")
include("script-host")
