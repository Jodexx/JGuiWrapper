dependencies {
    paperweight.paperDevBundle("1.20-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}