package gradle.plugins.web.js.karakum

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
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
