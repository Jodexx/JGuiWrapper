dependencies {
    compileOnly(fileTree("libs"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}