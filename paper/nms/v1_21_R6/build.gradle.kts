dependencies {
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT") // actually 1.21.10 - 1_21_R6; 1.21.11 - 1_21_R7
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}