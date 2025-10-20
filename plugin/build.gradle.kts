plugins {
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("com.gradleup.shadow")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":nms"))
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-20211218.082619-371")
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
    jvmArgs("-XX:+UseG1GC")
}

tasks.processResources {
    val props = mapOf("version" to project.version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}