package plugin.project.apple

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
import plugin.project.apple.model.AppleTarget
import plugin.project.kotlin.model.language.KotlinAndroidTarget
import plugin.project.kotlin.model.language.KotlinAppleTarget

internal class ApplePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets.orEmpty().none { target -> target is KotlinAppleTarget }) {
                return@with
            }

            extraProperties.set("generateBuildableXcodeproj.skipKotlinFrameworkDependencies", "true")

            plugins.apply(settings.libs.plugins.plugin("apple").id)

            projectProperties.apple.applyTo()
        }
    }
}
