plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    api(libs.plugin.android)
    api(libs.plugin.kotlin)
    api(libs.plugin.detekt)
}