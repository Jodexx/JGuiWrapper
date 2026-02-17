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

include(
    ":api",
    ":common",
    ":paper:paper-api",
    ":paper:paper-plugin",

    ":paper:nms",
    ":paper:nms:v1_16_R3",
    ":paper:nms:v1_17_R1",
    ":paper:nms:v1_18_R1",
    ":paper:nms:v1_18_R2",
    ":paper:nms:v1_19_R1",
    ":paper:nms:v1_19_R2",
    ":paper:nms:v1_19_R3",
    ":paper:nms:v1_20_R1",
    ":paper:nms:v1_20_R2",
    ":paper:nms:v1_20_R3",
    ":paper:nms:v1_20_R4",
    ":paper:nms:v1_21_R1",
    ":paper:nms:v1_21_R2",
    ":paper:nms:v1_21_R3",
    ":paper:nms:v1_21_R4",
    ":paper:nms:v1_21_R5",
    ":paper:nms:v1_21_R6",

    ":paper:paper-common"
)