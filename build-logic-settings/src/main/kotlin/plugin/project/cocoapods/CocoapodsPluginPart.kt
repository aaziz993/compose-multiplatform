package plugin.project.cocoapods

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.project.kotlin.model.target.TargetType
import plugin.project.kotlin.model.target.contains

internal class CocoapodsPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (TargetType.APPLE !in settings.projectProperties.kotlin.targets) {
                project.logger.warn(
                    "Unnecessary to enable cocoapods plugin when no apple targets represented. " +
                        "Module: ${project.name}",
                )
                return@with
            }

            if (!settings.projectProperties.kotlin.cocoapods.enabled) {
                return@with
            }

            plugins.apply(libs.plugins.cocoapods.get().pluginId)

            configureCocoapodsExtension()
        }
    }
}
