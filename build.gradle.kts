plugins {
    kotlin("jvm") version "1.9.20"
    java
    application
}

group = "org.itmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.rabbitmq:amqp-client:5.27.1")
    implementation("org.apache.activemq:activemq-broker:6.1.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("Main")
}