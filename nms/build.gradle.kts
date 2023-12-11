plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.0"
    id("io.papermc.paperweight.userdev") version "1.5.11" apply false
}

subprojects {
    plugins.apply("java")
    plugins.apply("java-library")
    plugins.apply("io.papermc.paperweight.userdev")

    repositories {
        mavenCentral()
    }
}

dependencies {
    subprojects.forEach { implementation(project(":nms:" + it.name, "reobf")) }
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