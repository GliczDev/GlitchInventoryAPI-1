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
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    implementation(project(":core"))
    project(":core").dependencyProject.subprojects
        .forEach { implementation(project(":core:" + it.name, "reobf")) }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("${rootProject.name}-${project.version}.jar")
    }
}

publishing {
    publications {
        create<MavenPublication>("publishMaven") {
            project.shadow.component(this)
        }
    }
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

bukkit {
    name = rootProject.name
    main = "me.glicz.inventoryapi.GlitchInventoryAPIPlugin"
    apiVersion = "1.18"
    author = "Glicz"
}