rootProject.name = "JGuiWrapper"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.screamingsandals.org/public/")
        maven("https://repo.screamingsandals.org/releases")
        maven("https://repo.screamingsandals.org/snapshots/")
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":api", ":plugin", ":nms", ":common")

file("nms").listFiles()
    ?.filter { it.isDirectory && File(it, "build.gradle.kts").isFile }
    ?.sortedBy { it.name }
    ?.forEach { dir ->
        include(":nms:${dir.name}")
        project(":nms:${dir.name}").projectDir = dir
    }
