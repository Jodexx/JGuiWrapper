dependencies {
    api(project(":paper:paper-api"))
    compileOnly(project(":paper:nms"))
    compileOnly("me.clip:placeholderapi:2.11.6")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifactId = "common"
        }
    }
}