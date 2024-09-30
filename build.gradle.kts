plugins {
    kotlin("jvm") version "2.0.20"
    id("maven-publish")
    id("signing")
}

group = "ch.bytecraft"
version = "2.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/ch.bytecraft/escaper
    implementation("ch.bytecraft:escaper:1.0.0")

    // https://mvnrepository.com/artifact/ch.bytecraft/cheks
    implementation("ch.bytecraft:cheks:1.0.0")

    // https://mvnrepository.com/artifact/ch.bytecraft/linked-stack
    implementation("ch.bytecraft:linked_stack:1.0.0")

    // https://mvnrepository.com/artifact/ch.bytecraft/position
    implementation("ch.bytecraft:position:1.0.0")

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.1")

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.1")

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.1")

    // https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:3.26.3")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

java {
    withJavadocJar()
    withSourcesJar()
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "ch.bytecraft"
            artifactId = "parser"
            version = "1.0.0"
        }

        withType<MavenPublication> {
            pom {
                packaging = "jar"
                name.set("Parser")
                description.set("A generic parser written in Kotlin")
                url.set("https://github.com/stefan-zemljic/parser")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("stefan-zemljic")
                        name.set("Stefan Zemljic")
                        email.set("stefan.zemljic@protonmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/stefan-zemljic/parser.git")
                    developerConnection.set("scm:git:ssh://github.com:stefan-zemljic/parser.git")
                    url.set("https://github.com/stefan-zemljic/parser")
                }
            }
        }
    }

    repositories {
        mavenLocal()
    }
}