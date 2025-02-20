package plugin.project.cocoapods

import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Project
import org.gradle.api.Plugin
import plugin.project.model.target.TargetType
import plugin.project.model.target.contains

internal class CocoapodsPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            moduleProperties.targets?.let { targets ->
                if (TargetType.APPLE !in targets) {
                    project.logger.warn(
                        "Unnecessary to enable cocoapods plugin when no apple targets represented. " +
                            "Module: ${project.name}",
                    )
                    return@with
                }

                if (!project.moduleProperties.settings.cocoapods.enabled) {
                    return@with
                }

                plugins.apply(libs.plugins.cocoapods.get().pluginId)

                configureCocoapodsExtension()
            }
        }
    }
}
