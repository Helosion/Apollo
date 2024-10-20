plugins {
    id("apollo.shadow-conventions")
    id("apollo.publish-conventions")
}

dependencies {
    api(project(path = ":apollo-api", configuration = "shadow"))

    api(libs.protobuf)
    api(libs.configurate.core)
    api(libs.configurate.yaml)
}

publishShadowJar()
