plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "9.0.0-beta15"

}

group = "com.jodexindustries"
version = "1.0.0"

dependencies {
    implementation(project("nms_wrapper"))
    implementation(project("nms_wrapper:v1_21_R4"))
    compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
}


tasks.named<xyz.jpenilla.runpaper.task.RunServer>("runServer") {
    minecraftVersion("1.21.5")
}

val targetJavaVersion = 21

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
}

tasks.jar {
    enabled = false;
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.shadowJar {
    archiveClassifier = ""
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}