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
    implementation("com.google.code.gson:gson:2.9.0")
}
