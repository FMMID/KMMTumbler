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

enableFeaturePreview("VERSION_CATALOGS")