dependencies {
    api(project(":nms"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifactId = "common"
        }
    }
}