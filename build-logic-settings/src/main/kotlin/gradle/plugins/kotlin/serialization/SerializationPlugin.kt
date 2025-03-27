package gradle.plugins.kotlin.serialization

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.serialization.model.SerializationSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SerializationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.serialization
                .takeIf(SerializationSettings::enabled)?.let { serialization ->
                    plugins.apply(project.settings.libs.plugins.plugin("kotlinx.serialization").id)

                    serialization.applyTo()
                }
        }
    }
}
