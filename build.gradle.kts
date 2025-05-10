plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.example"
version = "1.0.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "21"
    targetCompatibility = "21"
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("dev.folia:folia-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("dev.folia:folia:1.21.4-R0.1-SNAPSHOT")

    compileOnly("me.clip:placeholderapi:2.11.6")
}

tasks.withType<Jar> {
    archiveBaseName.set("Folia-Expansion")
    archiveClassifier.set("")
    archiveVersion.set("")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveClassifier.set("")
    relocate("me.clip.placeholderapi", "com.example.libs.placeholderapi")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
