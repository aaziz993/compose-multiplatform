package plugin.project.android

import gradle.kotlin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithPresetFunctions

internal fun Project.androidTarget() {
    kotlin {

        KotlinTargetContainerWithPresetFunctions.androidTarget(::configureKotlinAndroidTarget)

        sourceSets {
            androidMain.dependencies {

            }

            androidUnitTest.dependencies {
            }
        }
    }
}
