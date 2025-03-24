package plugins.kotlin.allopen.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.kotlin.allopen.AllOpenExtension
import gradle.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class AllOpenSettings(
    override val myAnnotations: List<String>? = null,
    override val myPresets: List<String>? = null,
    override val enabled: Boolean = true,
) : AllOpenExtension, EnabledSettings {

    context(project: Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("allopen").id) {
            super.applyTo()
        }
}
