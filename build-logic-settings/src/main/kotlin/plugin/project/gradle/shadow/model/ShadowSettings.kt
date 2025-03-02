package plugin.project.gradle.shadow.model

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.model.EnabledSettings

@Serializable
internal data class ShadowSettings(
    override val enabled: Boolean = true
) : EnabledSettings {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("kotlin.serialization").id) {

        }
}
