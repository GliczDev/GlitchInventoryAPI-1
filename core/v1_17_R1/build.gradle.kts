plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.3"
}

group = "me.glicz"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.17.1-R0.1-SNAPSHOT")
    compileOnly(project(":core"))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
}