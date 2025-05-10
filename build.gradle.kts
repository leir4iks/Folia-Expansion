plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.Folia-Expansion"
version = "1.0.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

}

dependencies {
    compileOnly("dev.folia:folia-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    jar {
        archiveBaseName.set("Folia-Expansion")
    }

    shadowJar {
        archiveClassifier.set("")
        relocate("me.clip.placeholderapi", "com.Folia-Expansion.shaded.placeholderapi")
    }

    build {
        dependsOn(shadowJar)
    }
}

