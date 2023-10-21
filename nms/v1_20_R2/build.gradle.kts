dependencies {
    paperweight.paperDevBundle("1.20.2-R0.1-SNAPSHOT")
    compileOnly(project(":core"))
    compileOnly(project(":nms:v1_17_R1"))
    compileOnly(project(":nms:v1_18_R1"))
    compileOnly(project(":nms:v1_19_R3"))
    compileOnly(project(":nms:v1_20_R1"))
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