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
    implementation("org.springframework.security:spring-security-crypto:5.7.1")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
    implementation("commons-logging:commons-logging:1.2")

    // GUI
    implementation("com.jgoodies:jgoodies-forms:1.9.0")
    implementation("com.jetbrains.intellij.java:java-gui-forms-rt:221.5921.27")

    // Other
    implementation("me.xdrop:fuzzywuzzy:1.4.0")
}
