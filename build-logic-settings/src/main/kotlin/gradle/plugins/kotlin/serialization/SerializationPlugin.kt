package gradle.plugins.kotlin.serialization

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.serialization.model.SerializationSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SerializationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.serialization?.takeIf{ pluginManager.hasPlugin("serialization") }?.let { serialization ->
                    plugins.apply(project.settings.libs.plugin("kotlinx.serialization").id)

                    serialization.applyTo()
                }
        }
    }
}
