plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":nms", "shadow"))
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
        archiveClassifier.set("")
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