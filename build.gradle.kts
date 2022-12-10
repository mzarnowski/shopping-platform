plugins {
    application
}

group = "dev.mzarnowski.shopping"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

task<Exec>("build-docker-image") {
    val dockerfile = projectDir.resolve("src/main/docker/Dockerfile")
    val tag = "${project.name}:${version}"
    commandLine("docker", "build", "--file" ,dockerfile, "--tag", tag, projectDir)
}

application {
    mainClass.set("dev.mzarnowski.shopping.product.pricing.Main")
    version = "" // simplifies containerization. We are not supporting multiple versions on a single container anyway
}
