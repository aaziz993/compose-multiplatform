package plugin.project.kotlin.allopen.model

import gradle.id
import gradle.libs
import gradle.model.kotlin.allopen.AllOpenExtension
import gradle.model.project.EnabledSettings
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class AllOpenSettings(
    override val myAnnotations: List<String>? = null,
    override val myPresets: List<String>? = null,
    override val enabled: Boolean = true,
) : AllOpenExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("allopen").id) {
            super.applyTo()
        }
}
