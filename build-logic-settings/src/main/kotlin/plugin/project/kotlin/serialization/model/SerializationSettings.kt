package plugin.project.kotlin.serialization.model

import gradle.id
import gradle.libs
import gradle.plugins.project.EnabledSettings
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class SerializationSettings(
    override val enabled: Boolean = true
) : EnabledSettings {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("shadow").id) {

        }
}
