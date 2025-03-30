package gradle.plugins.kotlin.serialization.model

import gradle.accessors.catalog.libs

import gradle.accessors.settings
import gradle.api.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class SerializationSettings(
    override val enabled: Boolean = true
) : EnabledSettings {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("kotlinx.serialization").id) {

        }
}
