dependencies {
    paperweight.paperDevBundle("1.18-R0.1-SNAPSHOT")
    compileOnly(project(":core"))
    compileOnly(project(":nms:v1_17_R1"))
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
}