package gradle.plugins.apple

import gradle.accessors.apple

import gradle.accessors.catalog.libs


import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.targets.nat.apple.KotlinAppleTarget
import gradle.plugins.project.ProjectLayout
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties

internal class ApplePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets.none { target -> target is KotlinAppleTarget }) {
                return@with
            }

            extraProperties["generateBuildableXcodeproj.skipKotlinFrameworkDependencies"] = "true"

            plugins.apply(project.settings.libs.plugin("apple").id)

            projectProperties.apple.applyTo()

            adjustSourceSets()
        }
    }

    private fun Project.adjustSourceSets() = when (projectProperties.layout) {
        is ProjectLayout.Flat -> apple.sourceSets.configureEach {
            apple.srcDir(
                if (sourceSetName.endsWith("AppMain"))
                    "src@${sourceSetName.removeSuffix("AppMain")}"
                else
                    "test@${sourceSetName.removeSuffix("AppTest")}",
            )
        }

        else -> Unit
    }
}
