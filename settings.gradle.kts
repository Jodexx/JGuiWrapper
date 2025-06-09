rootProject.name = "JGuiWrapper"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

include(":api", ":plugin", ":nms", ":common")

file("nms").listFiles()
    ?.filter { it.isDirectory && File(it, "build.gradle.kts").isFile }
    ?.sortedBy { it.name }
    ?.forEach { dir ->
        include(":nms:${dir.name}")
        project(":nms:${dir.name}").projectDir = dir
    }
