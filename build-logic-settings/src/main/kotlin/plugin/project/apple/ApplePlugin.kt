package plugin.project.apple

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import gradle.model.kmp.nat.apple.KotlinAppleTarget

internal class ApplePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets?.none { target -> target is KotlinAppleTarget } != false) {
                return@with
            }

            extraProperties.set("generateBuildableXcodeproj.skipKotlinFrameworkDependencies", "true")

            plugins.apply(settings.libs.plugins.plugin("apple").id)

            projectProperties.apple.applyTo()
        }
    }
}
