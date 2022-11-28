plugins {
    id("java")
    id("idea")
    id("com.diffplug.spotless") version "6.11.0"
}

group = "com.github.quaoz"
version = "0.1.0"

spotless {
    java {
        toggleOffOn()
        importOrder()
        removeUnusedImports()

        prettier(mapOf("prettier" to "2.7.1", "prettier-plugin-java" to "1.6.2")).config(mapOf("parser" to "java", "useTabs" to true))
        formatAnnotations()

        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlinGradle {
        ktlint()
        trimTrailingWhitespace()
        endWithNewline()
    }

    json {
        target("src/**/*.json")
        prettier(mapOf("prettier" to "2.7.1")).config(mapOf("parser" to "json"))
    }
}

repositories {
    mavenCentral()
    mavenLocal()

    // Maven repository for intellij java-gui-forms-rt
    maven {
        url = uri("https://www.jetbrains.com/intellij-repository/releases")
    }
}

dependencies {
    // Annotations
    implementation("org.jetbrains:annotations:23.0.0")

    // JSON
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.1")

    // CSV
    implementation("com.opencsv:opencsv:5.7.1")

    // Hashing
    implementation("de.mkammerer:argon2-jvm:2.11")

    // GUI
    implementation("com.jgoodies:jgoodies-forms:1.9.0")
    implementation("com.jetbrains.intellij.java:java-gui-forms-rt:222.4459.20")

    // Logging
    implementation("org.tinylog:tinylog-api:2.5.0")
    implementation("org.tinylog:tinylog-impl:2.5.0")

    // Fuzzy matching
    implementation("me.xdrop:fuzzywuzzy:1.4.0")

    // Geocoding
    implementation("com.byteowls:jopencage:1.4.0")

    // Dotenv
    implementation("io.github.cdimascio:dotenv-java:2.2.4")

    // FlatLaf
    implementation("com.formdev:flatlaf:2.6")
    implementation("com.formdev:flatlaf-intellij-themes:2.6")

    // Appdirs
    implementation("net.harawata:appdirs:1.2.1")
}

val fatJar = task("fatJar", type = Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.github.quaoz.Main"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(
        configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    )
    with(tasks.jar.get() as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}
