plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.0"
}

dependencies {
    project(":nms").dependencyProject.subprojects
        .forEach { implementation(project(":nms:" + it.name, "reobf")) }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(16)
    }
}