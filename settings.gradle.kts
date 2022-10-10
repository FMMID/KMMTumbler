pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "KMMTumbler"
include(":androidApp")
include(":shared")

includeBuild("buildLogic")

enableFeaturePreview("VERSION_CATALOGS")