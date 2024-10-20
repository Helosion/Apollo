plugins {
    id("apollo.shadow-conventions")
}

setupDynamicLoader()

dependencies {
    compileOnly(libs.bukkit)
    compileOnly(libs.protobuf)

    api(project(path = ":apollo-api", configuration = "shadow"))
    api(project(path = ":apollo-common", configuration = "shadow"))

    "loaderCompileOnly"(libs.bukkit.api)
    "loaderImplementation"(project(":extra:apollo-extra-loader"))
}
