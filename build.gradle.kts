plugins {
    id("com.gradleup.shadow") version "9.3.1" apply false
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19" apply false
}

var excludedProjects = listOf("paper")
var publishProjects = listOf("api", "common", "paper-common", "paper-api", "nms")

subprojects {

    if (name in excludedProjects) return@subprojects

    apply(plugin = "java")

    if (name in publishProjects) {
        apply(plugin = "java-library")
        apply(plugin = "maven-publish")
    }

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(16))
    }
}