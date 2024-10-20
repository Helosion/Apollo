plugins {
    id("apollo.shadow-conventions")
    id("apollo.publish-conventions")
}

setupPlatforms()

setupPlatformDependency("bukkit", "bukkitJar")

val main by sourceSets

dependencies {
    "shade"(libs.geantyref)

    "bukkit"(main.output)
    "bukkit"(libs.bukkit.api)
    "bukkit"(libs.bukkit)
}

publishShadowJar()
