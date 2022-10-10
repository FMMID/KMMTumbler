plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config = files("../buildlogic/detekt/config.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("../buildlogic/detekt/baseline.xml") // a way of suppressing issues before introducing detekt
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.16.0")
}

tasks {
    withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        reports {
            xml {
                outputLocation.set(file("build/reports/detekt-results.xml"))
            }
            html {
                outputLocation.set(file("build/reports/detekt-results.html"))
            }
            txt.required.set(false)
        }
    }
    withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "1.8"
    }
    withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
        jvmTarget = "1.8"
    }
    matching { it.name == "check" }.configureEach {
        dependsOn(detekt)
    }
}