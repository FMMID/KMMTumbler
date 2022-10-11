import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    kotlin("plugin.serialization")
    id("shared-configuration")
    id("com.android.library")
    id("io.gitlab.arturbosch.detekt")
}

kotlin {
    android()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //Paging
                api(libs.multiplatform.paging)

                //Coroutines
                implementation(libs.kotlinx.coroutines.core)

                //Serialization
                implementation(libs.kotlinx.serialization.core)

                //Logger
                implementation(libs.napier)

                //Ktor
                implementation(libs.ktor.core)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.client.auth)

                //SQLDelight
                implementation(libs.sqldelight)

                //Multiplatform cash
                implementation(libs.multiplatform.settings)

                //DI
                api(libs.koin.core)
            }
        }
        val androidMain by getting {
            dependencies {
                //Coroutines
                implementation(libs.kotlinx.coroutines.android)

                //Ktor
                implementation(libs.ktor.client.android)

                //SQLDelight
                implementation(libs.sqldelight.android)

                //Androidx
                implementation(libs.androidx.lifecycle.viewmodel.ktx)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                //Ktor
                implementation(libs.ktor.client.ios)

                //SQLDelight
                implementation(libs.sqldelight.ios)
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
    }
}

buildkonfig {
    //To generate BuildKonfig files, run ./gradlew generateBuildKonfig task.
    defaultConfigs {
        buildConfigField(STRING, "REDIRECT_URI", "https://www.google.ru/")
        buildConfigField(STRING, "CLIENT_CONSUMER_KEY", "JPXGabRtZkIVfuaI0vBCXiFFoa4X6vsnFWHlpAxrtm1QYpShRB")
        buildConfigField(STRING, "CLIENT_SECRET_KEY", "OM589BCr8mP7Mn9coXFF2yqJlgNTpsLBUdjiOhRPSWFDMT97S4")
    }
}

tasks.register("detektAll") {
    allprojects {
        this@register.dependsOn(tasks.withType<io.gitlab.arturbosch.detekt.Detekt>())
    }
}