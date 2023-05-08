import com.mooltiverse.oss.nyx.gradle.*

rootProject.name = "webmonitor"

plugins {
    id("com.mooltiverse.oss.nyx") version "2.0.0"
}

extensions.configure<NyxExtension>("nyx") {
    preset.set("simple")

//    dryRun.set(false)
//    resume.set(false)
//    stateFile.set(".nyx-state.yml")
//    verbosity.set("DEBUG")


    changelog {
        path.set("build/CHANGELOG.md")
    }
}
