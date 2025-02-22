package plugin.project.gradle.spotless

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SpotlessPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!projectProperties.plugins.spotless.enabled ||projectProperties.kotlin.hasTargets) {
                return@with
            }

            plugins.apply(project.libs.plugins.spotless.get().pluginId)

            configureSpotlessExtension()
        }
    }
}
