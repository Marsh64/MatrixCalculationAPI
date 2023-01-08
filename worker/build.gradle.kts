import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
    kotlin("plugin.serialization") version "1.7.10" // must have
}

group = "org.distributedSystem"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    //concurrency
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    //serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    //RabbitMQ
    implementation("com.rabbitmq:amqp-client:5.15.0")

    //to remove logger warnings
    implementation("org.slf4j:slf4j-simple:2.0.0")
    implementation("org.slf4j:slf4j-api:2.0.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

tasks.jar {
    archiveFileName.set("Worker.jar")
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}