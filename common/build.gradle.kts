val nmsProjects = project(":nms").childProjects.values

dependencies {
    api(project(":api"))

    nmsProjects.forEach { nmsProject ->
        val nmsName = nmsProject.name
        val reobfJarTaskPath = ":nms:$nmsName:reobfJar"
        val reobfJarPath = "${nmsProject.projectDir}/build/libs/${nmsName}-${version}-reobf.jar"

        val reobfJarFile = files(reobfJarPath).builtBy(reobfJarTaskPath)

        api(reobfJarFile)
    }
}

tasks.jar {
    enabled = false;
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}