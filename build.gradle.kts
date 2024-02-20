
plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.9.2")
    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    implementation("com.hexadevlabs:gpt4all-java-binding:1.1.5")
}

tasks.test {
    useJUnitPlatform()
}