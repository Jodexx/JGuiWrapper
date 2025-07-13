dependencies {
    api(project(":nms"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifactId = "common"
        }
    }
}