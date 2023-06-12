plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly(project(":core"))
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    implementation(project(":shade", "shadow"))
    library("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    library("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(16)
        dependsOn(clean)
    }

    shadowJar {
        archiveClassifier.set("plugin")
        archiveBaseName.set(rootProject.name)
    }
}

bukkit {
    name = rootProject.name
    main = "me.glicz.inventoryapi.plugin.GlitchInventoryAPIPlugin"
    apiVersion = "1.17"
    author = "Glicz"
}