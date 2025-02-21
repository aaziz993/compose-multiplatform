package plugin.project.kotlinnative

import gradle.kotlin
import gradle.moduleProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

internal fun Project.configureKotlinNativeTarget() =
    kotlin.targets.withType<KotlinNativeTarget> {
        moduleProperties.settings.native.let { native ->
            native.binaries?.let { binaries ->
                binaries {
                    binaries.framework?.let { framework ->
                        framework {
                            framework.applyTo(this)
                        }
                    }
                }
            }
        }
    }
