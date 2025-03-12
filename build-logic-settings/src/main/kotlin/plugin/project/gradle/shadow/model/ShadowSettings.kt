package plugin.project.gradle.shadow.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.plugins.project.EnabledSettings
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class ShadowSettings(
    override val enabled: Boolean = true
) : EnabledSettings {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("kotlin.serialization").id) {

        }
}
