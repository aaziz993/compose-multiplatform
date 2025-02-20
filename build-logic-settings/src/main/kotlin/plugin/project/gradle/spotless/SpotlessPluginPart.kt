package plugin.project.gradle.spotless

import gradle.moduleProperties
import gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SpotlessPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.gradle.spotless.enabled || moduleProperties.targets == null) {
                return@with
            }

            plugins.apply(project.libs.plugins.spotless.get().pluginId)

            configureSpotlessExtension()
        }
    }
}
