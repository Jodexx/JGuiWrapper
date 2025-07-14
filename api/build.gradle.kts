dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-20211218.082619-371")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))

    withSourcesJar()
    withJavadocJar()
}

tasks.withType<Javadoc> {
    javadocTool.set(javaToolchains.javadocToolFor {
        languageVersion.set(JavaLanguageVersion.of(17))
    })

    options {
        encoding = "UTF-8"
        (this as StandardJavadocDocletOptions).apply {
            source = "8"
            addBooleanOption("html5", true)
            addBooleanOption("Xdoclint:none", true)
            links = listOf(
                "https://docs.oracle.com/en/java/javase/17/docs/api/",
                "https://jd.papermc.io/paper/1.16.5/",
                "https://jd.advntr.dev/api/4.7.0/"
            )
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifactId = "api"
        }
    }
}
