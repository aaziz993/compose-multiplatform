/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.apple

import gradle.kotlin
import gradle.libs
import gradle.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties

internal class ApplePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
//            if (TargetType.APPLE !in projectProperties.kotlin.targets) {
//                return@with
//            }
//
//            kotlin.iosSimulatorArm64(){
//
//            }
//
//            plugins.apply(libs.plugins.apple.get().pluginId)
//plugins.apply(libs.plugins.cocoapods.get().pluginId)
//           projectProperties.kotlin.targets.filter { (type, _) -> type.isDescendantOf(TargetType.APPLE) }
//                .forEach { target -> target.applyTo() }
//
//            extraProperties.set("generateBuildableXcodeproj.skipKotlinFrameworkDependencies", "true")
//
//            configureAppleProjectExtension()
            kotlin.mingwX64{
                this.isSourcesPublishable=true
            }
        }
    }
}
