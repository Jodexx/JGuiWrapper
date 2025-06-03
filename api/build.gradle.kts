val nmsWrapper = project(":nms_wrapper")
val nmsProjects = nmsWrapper.childProjects.values

dependencies {
    api(project(":nms_wrapper"))

    nmsProjects.forEach { nmsProject ->
        val nmsName = nmsProject.name
        val reobfJarPath = "$nmsName/build/libs/${nmsName}-${version}-reobf.jar"

        val reobfJarFiles = nmsWrapper.files(reobfJarPath).builtBy(":nms_wrapper:$nmsName:reobfJar")

        api(reobfJarFiles)
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}