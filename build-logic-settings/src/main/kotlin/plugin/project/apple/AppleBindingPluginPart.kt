/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.apple

import gradle.kotlin
import org.gradle.api.Plugin
import org.gradle.api.Project

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
//            plugins.apply(settings.libs.plugins.plugin("apple").id)
//plugins.apply(settings.libs.plugins.plugin("cocoapods").id)
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
