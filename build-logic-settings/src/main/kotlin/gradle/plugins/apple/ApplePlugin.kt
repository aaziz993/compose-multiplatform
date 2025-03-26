package gradle.plugins.apple

import gradle.accessors.apple
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kmp.nat.apple.KotlinAppleTarget
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

            extraProperties.set("generateBuildableXcodeproj.skipKotlinFrameworkDependencies", "true")

            plugins.apply(project.settings.libs.plugins.plugin("apple").id)

            projectProperties.apple.applyTo()

            adjustSourceSets()
        }
    }

    private fun Project.adjustSourceSets() = when (projectProperties.layout) {
        ProjectLayout.FLAT -> apple.sourceSets.configureEach {
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
