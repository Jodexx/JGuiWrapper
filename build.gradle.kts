plugins {
    java
    `java-library`
    id("com.gradleup.shadow") version "9.0.0-beta15" apply false
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17" apply false

}

tasks.jar {
    enabled = false;
}

allprojects {
    group = "com.jodexindustries.jguiwrapper"
    version = "1.0.0"

    apply(plugin = "java-library")
    apply(plugin = "com.gradleup.shadow")

    repositories {
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

subprojects {
    if (path.startsWith(":nms:")) {
        apply(plugin = "io.papermc.paperweight.userdev")
    }
}