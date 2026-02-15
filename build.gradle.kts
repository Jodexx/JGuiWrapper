plugins {
    java
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.3.1" apply false
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19" apply false
}

tasks.jar {
    enabled = false
}

var excludedProjects = listOf("paper")
var paperProjects = listOf("paper-common", "paper-api", "nms")

subprojects {
    if (name !in excludedProjects) {
        apply(plugin = "java")
        apply(plugin = "java-library")

        if (name in paperProjects) {
            apply(plugin = "maven-publish")

            afterEvaluate {
                dependencies.compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-20211218.082619-371")
            }
        }

        java {
            toolchain {
                languageVersion = JavaLanguageVersion.of(16)
            }
        }
    }
}

allprojects {
    group = "com.jodexindustries.jguiwrapper"
    version = "1.0.0.9"

    repositories {
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.extendedclip.com/releases/")
        maven("https://repo.screamingsandals.org/public")
        maven("https://repo.screamingsandals.org/releases")
        maven("https://repo.screamingsandals.org/snapshots/")
    }

}