enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}
includeBuild("buildLogic")

rootProject.name = "KMMTumbler"
include(":androidApp")
include(":shared")