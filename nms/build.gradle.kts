val useReobfJar = project.properties["useReobfJar"]?.toString()?.toBoolean() ?: true

dependencies {
    api(project(":api"))
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

val nmsProjects = childProjects.keys.map { ":nms:$it" }

nmsProjects.forEach { path ->
    evaluationDependsOn(path)
    val nmsProject = project(path)

    if (useReobfJar) {
        val reobfJarTask = nmsProject.tasks.named("reobfJar")

        val jarFileProvider = reobfJarTask.map { task -> task.outputs.files }

        dependencies {
            api(nmsProject.files(jarFileProvider).builtBy(reobfJarTask))
        }
    } else {
        dependencies {
            api(nmsProject)
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}