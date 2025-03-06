package plugin.project.kotlin.allopen.model

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.model.EnabledSettings

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
