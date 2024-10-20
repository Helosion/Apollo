plugins {
    id("com.github.johnrengelman.shadow")
    id("apollo.publish-conventions")
}

setupAdventureProject()

dependencies {
    api(libs.bundles.adventure) {
        exclude("org.checkerframework")
    }
}

publishJar()
