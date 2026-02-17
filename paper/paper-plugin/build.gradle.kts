plugins {
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("com.gradleup.shadow")
}

dependencies {
    implementation(project(":paper:paper-common"))
    implementation(project(":paper:nms"))
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    archiveBaseName.set("JGuiWrapper")
    archiveClassifier.set("")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.runServer {
    minecraftVersion("1.16.5")
    jvmArgs("-DPaper.IgnoreJavaVersion=true")
}

tasks.withType<ProcessResources>().configureEach {
    val props = mapOf("version" to project.version)

    inputs.properties(props)

    filesMatching("plugin.yml") {
        expand(props)
    }
}