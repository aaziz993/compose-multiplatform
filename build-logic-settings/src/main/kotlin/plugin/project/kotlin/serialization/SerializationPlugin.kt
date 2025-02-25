package plugin.project.kotlin.serialization

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SerializationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.serialization
                .takeIf { it.enabled && projectProperties.kotlin.targets?.isNotEmpty()==true }?.let { serialization ->
                    plugins.apply(settings.libs.plugins.plugin("kotlin.serialization").id)

                    serialization.applyTo()
                }
        }
    }
}
