plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.3"
}

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.18.2-R0.1-SNAPSHOT")
    compileOnly(project(":core"))
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