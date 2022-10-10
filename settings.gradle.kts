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

includeBuild("buildlogic")

enableFeaturePreview("VERSION_CATALOGS")