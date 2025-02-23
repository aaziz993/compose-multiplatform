/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.nat

import gradle.kotlin
import gradle.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class NativePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!projectProperties.kotlin.hasNativeTargets) {
                return@with
            }

            projectProperties.kotlin.androidNativeArm32?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.androidNativeArm32(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.androidNativeArm32 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.androidNativeArm64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.androidNativeArm64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.androidNativeArm64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.androidNativeX86?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.androidNativeX86(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.androidNativeX86 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.androidNativeX64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.androidNativeX64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.androidNativeX64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.linuxArm64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.linuxArm64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.linuxArm64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.linuxX64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.linuxX64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.linuxX64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.mingwX64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.mingwX64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.mingwX64 {
                    target.applyTo(this)
                }
            }
        }
    }
}
