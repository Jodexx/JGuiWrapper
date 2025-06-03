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

include(
    ":nms_wrapper",
    ":nms_wrapper:v1_21_R4",
    ":nms_wrapper:v1_21_R3",
    ":nms_wrapper:v1_21_R2",
    ":nms_wrapper:v1_21_R1",
    ":nms_wrapper:v1_20_R4",
    ":nms_wrapper:v1_20_R3",
    ":nms_wrapper:v1_20_R2",
    ":nms_wrapper:v1_20_R1",
    ":api",
    ":plugin"
)
