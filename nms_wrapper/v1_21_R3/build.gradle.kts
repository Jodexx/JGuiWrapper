dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    compileOnly(project(":nms_wrapper"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}