plugins {
    kotlin("jvm") version "1.9.22"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.keremgokhan"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Javalin
    implementation("io.javalin:javalin:6.1.3")

    // Template engine - Jte
    implementation("gg.jte:jte:3.1.9")
    implementation("io.javalin:javalin-rendering:6.1.3")

    // Database
    implementation("org.jetbrains.exposed:exposed-core:0.46.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.46.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.46.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.46.0")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("com.zaxxer:HikariCP:5.1.0")

    // Security
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.owasp.java-html-sanitizer:owasp-java-html-sanitizer:20220608.1")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    // Configuration
    implementation("com.typesafe:config:1.4.3")

    // Testing
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("io.javalin:javalin-testtools:6.1.3")
    testImplementation("com.h2database:h2:2.2.224")
}

application {
    mainClass.set("com.keremgokhan.blog.ApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.keremgokhan.blog.ApplicationKt"
    }
}
