package plugin.project.gradle.spotless.model

import com.diffplug.spotless.LineEnding
import gradle.id
import gradle.libs
import gradle.model.gradle.spotless.FormatExtension
import gradle.model.gradle.spotless.FormatExtensionTransformingSerializer
import gradle.model.gradle.spotless.SpotlessExtension
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.project.EnabledSettings

@Serializable
internal data class SpotlessSettings(
    override val lineEndings: LineEnding? = null,
    override val encoding: String? = null,
    override val ratchetFrom: String? = null,
    override val enforceCheck: Boolean? = null,
    override val predeclareDepsFromBuildscript: Boolean? = null,
    override val predeclareDeps: Boolean? = null,
    override val formats: LinkedHashSet<@Serializable(with = FormatExtensionTransformingSerializer::class) FormatExtension>? = null,
    override val enabled: Boolean = true
) : SpotlessExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("spotless").id) {
            super.applyTo()
        }
}
