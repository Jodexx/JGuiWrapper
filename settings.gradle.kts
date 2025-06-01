rootProject.name = "JGuiWrapper"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

}

include("nms_wrapper")
include("nms_wrapper:v1_21_R4")