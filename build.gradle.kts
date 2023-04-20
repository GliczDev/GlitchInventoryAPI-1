plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
}

dependencies {
    implementation(project(":plugin"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(16)
    }

    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("${project.name}-${project.version}.jar")
    }
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("publishMaven") {
                shadow.component(this)
            }
        }
    }
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

bukkit {
    main = "me.glicz.inventoryapi.GlitchInventoryAPIPlugin"
    apiVersion = "1.17"
    author = "Glicz"
}