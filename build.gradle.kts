plugins {
    id("java")
    id("idea")
    idea
}

group = "com.github.quaoz"
version = "1.0-SNAPSHOT"

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
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")

    // Hashing
    implementation("de.mkammerer:argon2-jvm:2.11")

    // GUI
    implementation("com.jgoodies:jgoodies-forms:1.9.0")
    implementation("com.jetbrains.intellij.java:java-gui-forms-rt:221.5921.27")

    // Logging
    implementation("org.tinylog:tinylog-api:2.5.0")
    implementation("org.tinylog:tinylog-impl:2.5.0")

    // Fuzzy matching
    implementation("me.xdrop:fuzzywuzzy:1.4.0")

    // Geocoding
    implementation("com.byteowls:jopencage:1.4.0")

    // Dotenv
    implementation("io.github.cdimascio:dotenv-java:2.2.4")
}
