/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.nat

import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.apple.configureAppleProjectExtension

internal class NativePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!projectProperties.kotlin.hasNativeTargets) {
                return@with
            }

            projectProperties.kotlin.androidNativeArm32?.forEach { targetName, target ->
                targetName.takeIf(String::isNotEmpty)?.also { targetName ->
                    kotlin.androidNativeArm32(targetName) {
                        target.applyTo(this)
                    }
                } ?: kotlin.androidNativeArm32 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.androidNativeArm64?.forEach { targetName, target ->
                targetName.takeIf(String::isNotEmpty)?.also { targetName ->
                    kotlin.androidNativeArm64(targetName) {
                        target.applyTo(this)
                    }
                } ?: kotlin.androidNativeArm64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.androidNativeX86?.forEach { targetName, target ->
                targetName.takeIf(String::isNotEmpty)?.also { targetName ->
                    kotlin.androidNativeX86(targetName) {
                        target.applyTo(this)
                    }
                } ?: kotlin.androidNativeX86 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.androidNativeX64?.forEach { targetName, target ->
                targetName.takeIf(String::isNotEmpty)?.also { targetName ->
                    kotlin.androidNativeX64(targetName) {
                        target.applyTo(this)
                    }
                } ?: kotlin.androidNativeX64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.linuxArm64?.forEach { targetName, target ->
                targetName.takeIf(String::isNotEmpty)?.also { targetName ->
                    kotlin.linuxArm64(targetName) {
                        target.applyTo(this)
                    }
                } ?: kotlin.linuxArm64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.linuxX64?.forEach { targetName, target ->
                targetName.takeIf(String::isNotEmpty)?.also { targetName ->
                    kotlin.linuxX64(targetName) {
                        target.applyTo(this)
                    }
                } ?: kotlin.linuxX64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.mingwX64?.forEach { targetName, target ->
                targetName.takeIf(String::isNotEmpty)?.also { targetName ->
                    kotlin.mingwX64(targetName) {
                        target.applyTo(this)
                    }
                } ?: kotlin.mingwX64 {
                    target.applyTo(this)
                }
            }
        }
    }
}
