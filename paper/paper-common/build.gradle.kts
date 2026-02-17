dependencies {
    api(project(":paper:paper-api"))
    compileOnly(project(":paper:nms"))
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifactId = "common"
        }
    }
}