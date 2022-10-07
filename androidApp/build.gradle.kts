plugins {
    kotlin("android")
    id("com.android.application")
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()

        applicationId = "com.app.kmmtumbler.android"
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["appAuthRedirectScheme"] = "https://localhost/"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.android.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constain.layout)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //Coroutines
    implementation(libs.kotlinx.coroutines.android)

    //DI
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    //Paging
    implementation(libs.androidx.paging)

    //Yookass
    implementation(libs.yookassa.android)
}

dependencies{
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.16.0")
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config = files("$projectDir/code_quality/config.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/code_quality/baseline.xml") // a way of suppressing issues before introducing detekt
}