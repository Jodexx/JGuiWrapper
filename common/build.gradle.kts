dependencies {
    api(project(":nms"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}