plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17"
}

group = "com.jodexindustries"
version = "1.0.0"

dependencies {
    paperweight.paperDevBundle("1.21.5-R0.1-SNAPSHOT")
    implementation(project.parent!!)
}

val targetJavaVersion = 21

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
}

