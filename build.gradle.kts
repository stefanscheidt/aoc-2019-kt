plugins {
    val kotlinVersion = "1.3.61"
    kotlin("jvm") version kotlinVersion
}

repositories {
    mavenCentral()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs += arrayOf("-Xinline-classes")
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

dependencies {
    val junitVersion = "5.5.2"
    val awaitilityVersion = "4.0.1"

    implementation(kotlin("stdlib-jdk8"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    testImplementation("org.assertj:assertj-core:3.14.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.awaitility:awaitility:$awaitilityVersion")
    testImplementation("org.awaitility:awaitility-kotlin:$awaitilityVersion")
}