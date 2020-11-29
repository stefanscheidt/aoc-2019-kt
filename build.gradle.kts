plugins {
    kotlin("jvm") version "1.4.20"
    id("com.github.ben-manes.versions") version "0.36.0"
}

repositories {
    mavenCentral()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
        kotlinOptions.freeCompilerArgs += arrayOf("-Xinline-classes")
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

dependencies {
    val awaitilityVersion = "4.0.3"
    val assertjVersion = "3.18.1"
    val junitVersion = "5.7.0"

    implementation(kotlin("stdlib-jdk8"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    testImplementation("org.assertj:assertj-core:$assertjVersion")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.awaitility:awaitility:$awaitilityVersion")
    testImplementation("org.awaitility:awaitility-kotlin:$awaitilityVersion")
}