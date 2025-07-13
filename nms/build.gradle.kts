val excludedProjects = listOf(":nms:v1_16_R3")

dependencies {
    api(project(":api"))
    compileOnlyApi("com.destroystokyo.paper:paper-api:1.16.5-R0.1-20211218.082619-371")
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
            compileOnly(project(":api"))
        }

        if (!excludedProjects.contains(path)) {
            plugins.apply("io.papermc.paperweight.userdev")

            tasks.named("assemble") {
                dependsOn(tasks.named("reobfJar"))
            }
        } else {
//            plugins.apply("me.kcra.takenaka.accessor")
        }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "nms"
        }
    }
}