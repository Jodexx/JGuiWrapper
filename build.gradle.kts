import org.gradle.kotlin.dsl.invoke

plugins {
    java
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.0.2" apply false
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18" apply false
}

tasks.jar {
    enabled = false
}

var publishProjects = listOf("common", "api", "nms")

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    if (name in publishProjects) {
        java {
            toolchain.languageVersion.set(JavaLanguageVersion.of(8))
        }

        afterEvaluate {
            dependencies.compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-20211218.082619-371")

            publishing {
                repositories {
                    maven {
                        url = uri("https://repo.jodex.xyz/releases")
                        credentials {
                            username = findProperty("jodexRepoUser") as String? ?: System.getenv("JODEX_REPO_USER")
                            password = findProperty("jodexRepoPassword") as String? ?: System.getenv("JODEX_REPO_PASSWORD")
                        }
                    }
                }
            }
        }
    }
}

allprojects {
    group = "com.jodexindustries.jguiwrapper"
    version = "1.0.0.4"

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