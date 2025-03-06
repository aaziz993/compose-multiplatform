package plugin.project.web.js.karakum.model

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class KarakumSettings(
    override val configFile: String? = null,
    override val extensionSource: String? = null,
) : KarakumExtension {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("karakum").id) {
            super.applyTo()
        }
}
