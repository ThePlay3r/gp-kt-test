plugins {
    kotlin("jvm") version "1.6.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(group = "cz.gopay", name = "gp-java-api-v3-common", version = "3.4.10")
    implementation(group = "cz.gopay", name = "gp-java-api-v3-apache-http-client", version = "3.4.10")
}