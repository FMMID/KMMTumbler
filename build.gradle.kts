buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.bundles.plugins)
        classpath(":buildLogic")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }

    // run ./gradlew dependencyUpdates
    // Report: build/dependencyUpdates/report.txt
    apply(plugin = "com.github.ben-manes.versions")
    apply(plugin = "io.gitlab.arturbosch.detekt")
}

//https://github.com/ben-manes/gradle-versions-plugin#rejectversionsif-and-componentselection
fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
    withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
        rejectVersionIf {
            isNonStable(candidate.version) && !isNonStable(currentVersion)
        }
    }
}