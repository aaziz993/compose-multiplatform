package plugins.kotlin.serialization

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SerializationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.serialization
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { serialization ->
                    plugins.apply(project.settings.libs.plugins.plugin("kotlin.serialization").id)

                    serialization.applyTo()
                }
        }
    }
}
