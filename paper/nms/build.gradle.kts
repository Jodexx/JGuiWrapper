val excludedProjects = listOf(":paper:nms:v1_16_R3")

dependencies {
    api(project(":paper:paper-api"))
}

tasks.jar {
    childProjects.values.forEach { subproject ->
        if (subproject.parent == project) {
            evaluationDependsOn(subproject.path)

            val task = if (!excludedProjects.contains(subproject.path)) {
                subproject.tasks.named("reobfJar")
            } else {
                subproject.tasks.named("jar")
            }

            dependsOn(task)

            from(zipTree(task.get().outputs.files.singleFile))
        }
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

subprojects {
    dependencies {
        compileOnly(project(":paper:paper-api"))
    }

    if (!excludedProjects.contains(path)) {
        plugins.apply("io.papermc.paperweight.userdev")

        tasks.named("assemble") {
            dependsOn(tasks.named("reobfJar"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "nms"
        }
    }
}