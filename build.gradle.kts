plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
}

group = "org.dreeam.expansion.folia"

    maven {
        name = "placeholderapi"
        url = uri("https://repo.helpch.at/#/releases/me/clip/placeholderapi/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6") {
        exclude(group = "org.bstats")
    }

    paperweight.foliaDevBundle("1.21.4-R0.1-SNAPSHOT")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
