package plugin.project.kotlin.allopen.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.plugins.kotlin.allopen.AllOpenExtension
import gradle.plugins.project.EnabledSettings
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
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
