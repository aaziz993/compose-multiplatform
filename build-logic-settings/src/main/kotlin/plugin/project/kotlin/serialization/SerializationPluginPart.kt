package plugin.project.kotlin.serialization

import gradle.moduleProperties
import gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SerializationPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.kotlin.serialization.enabled || moduleProperties.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.kotlin.serialization.get().pluginId)
        }
    }
}
