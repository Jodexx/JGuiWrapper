val useReobfJar = project.properties["useReobfJar"]?.toString()?.toBoolean() ?: true

dependencies {
    api(project(":api"))
}

tasks.jar {
    childProjects.values.forEach { subproject ->
        if (subproject.parent == project) {
            evaluationDependsOn(subproject.path)

            val task = if (useReobfJar) {
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
    apply(plugin = "io.papermc.paperweight.userdev")

    dependencies {
        compileOnly(project(":api"))
    }

    if (useReobfJar) {
        tasks.named("assemble") {
            dependsOn(tasks.named("reobfJar"))
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "nms"
        }
    }
}