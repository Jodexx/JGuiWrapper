rootProject.name = "JGuiWrapper"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.extendedclip.com/releases/")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":api", ":paper:paper-api", ":paper:paper-plugin", ":paper:nms", ":paper:paper-common")

file("paper/nms").listFiles()
    ?.filter { it.isDirectory && File(it, "build.gradle.kts").isFile }
    ?.sortedBy { it.name }
    ?.forEach { dir ->
        include(":paper:nms:${dir.name}")
        project(":paper:nms:${dir.name}").projectDir = dir
    }