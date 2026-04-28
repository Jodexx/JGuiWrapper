repositories {
    mavenCentral()
}

dependencies {
    api(project(":common"))

    implementation("net.minestom:minestom:2026.04.13-1.21.11")

    testImplementation("net.minestom:testing:2026.04.11-1.21.11")
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    withSourcesJar()
    withJavadocJar()

    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.test {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifactId = "minestom"
        }
    }
}