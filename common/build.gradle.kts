dependencies {
    api(project(":api"))
    compileOnly(project(":nms"))
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