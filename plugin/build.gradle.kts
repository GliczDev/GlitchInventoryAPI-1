plugins {
    id("java")
    id("maven-publish")
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

publishing {
    publications {
        create<MavenPublication>("publishMaven") {
            shadow.component(this)
        }
    }
}

bukkit {
    name = rootProject.name
    main = "me.glicz.inventoryapi.plugin.GlitchInventoryAPIPlugin"
    apiVersion = "1.17"
    author = "Glicz"
}