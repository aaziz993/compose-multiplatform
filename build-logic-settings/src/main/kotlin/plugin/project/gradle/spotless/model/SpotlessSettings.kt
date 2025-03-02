package plugin.project.gradle.spotless.model

import com.diffplug.spotless.LineEnding
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import gradle.spotless
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.model.EnabledSettings

@Serializable
internal data class SpotlessSettings(
    override val lineEndings: LineEnding? = null,
    override val encoding: String? = null,
    override val ratchetFrom: String? = null,
    override val enforceCheck: Boolean? = null,
    override val predeclareDepsFromBuildscript: Boolean? = null,
    override val predeclareDeps: Boolean? = null,
    override val formats: Map<String, FormatSettings>? = null,
    override val kotlinGradle: KotlinGradleExtension? = null,
    override val enabled: Boolean = true
) : SpotlessExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("spotless").id) {
            super.applyTo()
        }
}
