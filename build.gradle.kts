plugins {
    id("java")
}

group = "com.github.quaoz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    implementation("me.xdrop:fuzzywuzzy:1.4.0")
}
