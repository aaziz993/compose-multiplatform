package plugins.karakum.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.karakum.KarakumExtension
import gradle.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class KarakumSettings(
    override val configFile: String? = null,
    override val extensionSource: String? = null,
    override val enabled: Boolean = true
) : KarakumExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("karakum").id) {
            super.applyTo()
        }
}
