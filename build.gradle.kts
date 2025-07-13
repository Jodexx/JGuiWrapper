plugins {
    java
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.0.0-beta15" apply false
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17" apply false
    id("me.kcra.takenaka.accessor") version "2.0.0-SNAPSHOT" apply false

}

tasks.jar {
    enabled = false;
}

var publishProjects = listOf("common", "api", "nms")

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    if (name in publishProjects) {
        afterEvaluate {
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
    version = "1.0.1"

    repositories {
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.screamingsandals.org/public")
        maven("https://repo.screamingsandals.org/releases")
        maven("https://repo.screamingsandals.org/snapshots/")
    }
}