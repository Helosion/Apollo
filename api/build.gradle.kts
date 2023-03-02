import com.google.protobuf.gradle.*

buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.19")
    }
}

plugins {
    id("java")
    `java-library`
    id("com.google.protobuf") version "0.8.19"
    id("idea")
    id("maven-publish")
}

group = "com.moonsworth"
version = "1.0-SNAPSHOT"

// TODO: enforce this works with Java 8
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

repositories {
    mavenCentral()
}

publishing {
    repositories {
        maven {
            url = uri("s3://" + System.getenv("REPO_BUCKET"))
            credentials(AwsCredentials::class) {
                accessKey = System.getenv("REPO_ACCESS_KEY")
                secretKey = System.getenv("REPO_SECRET_KEY")
            }
        }
    }
}

val lombokVersion: String by project
val protocVersion: String by project

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protocVersion"
    }
    generateProtoTasks {
        all().configureEach {
            // The generateProto task does not seem to properly clean its previously generated outputs.
            // See https://github.com/google/protobuf-gradle-plugin/issues/332
            // See https://github.com/google/protobuf-gradle-plugin/issues/331
            this.doFirst {
                delete(this.outputs)
            }
        }
    }
}
dependencies {
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // these dependencies match vanilla 1.19
    compileOnlyApi("io.netty:netty-transport-native-unix-common:4.1.77.Final")
    compileOnlyApi("io.netty:netty-transport-classes-epoll:4.1.77.Final")
    compileOnlyApi("io.netty:netty-transport:4.1.77.Final")
    compileOnlyApi("io.netty:netty-common:4.1.77.Final")
    compileOnlyApi("io.netty:netty-resolver:4.1.77.Final")
    compileOnlyApi("io.netty:netty-codec:4.1.77.Final")
    compileOnlyApi("io.netty:netty-handler:4.1.77.Final")
    compileOnlyApi("io.netty:netty-buffer:4.1.77.Final")
    compileOnlyApi("io.netty:netty-transport-native-epoll:4.1.77.Final")
    compileOnlyApi("com.google.guava:failureaccess:1.0.1")
    compileOnlyApi("com.google.guava:guava:31.0.1-jre")
    compileOnlyApi("org.apache.commons:commons-lang3:3.12.0")
    compileOnlyApi("commons-io:commons-io:2.11.0")
    compileOnlyApi("commons-codec:commons-codec:1.15")
    compileOnlyApi("com.google.code.gson:gson:2.8.9")
    compileOnlyApi("org.apache.logging.log4j:log4j-api:2.17.1")

    // shaded in dependencies
    api("com.google.protobuf:protobuf-java:3.21.4")
}

//val sourcesJar by tasks.registering(Jar::class) {
//    classifier = "sources"
//    from(sourceSets.main.get().allSource)
//}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
//            artifact(sourcesJar)
        }
    }
}
