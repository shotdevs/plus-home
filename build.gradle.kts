plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.plushome.mc"
version = "1.0.3"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

// Use Gradle Java toolchain so Gradle can provision a JDK 17 if none is installed
java {
    toolchain {
        languageVersion.set(org.gradle.jvm.toolchain.JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("org.xerial:sqlite-jdbc:3.36.0.3")
}

tasks.getByName<JavaCompile>("compileJava") {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    archiveBaseName.set("plushome")
    archiveClassifier.set("")
    archiveVersion.set(project.version.toString())
}
