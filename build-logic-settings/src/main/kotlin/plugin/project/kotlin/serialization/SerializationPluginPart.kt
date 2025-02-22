package plugin.project.kotlin.serialization

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SerializationPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.plugins.serialization.enabled || projectProperties.kotlin.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.kotlin.serialization.get().pluginId)
        }
    }
}
