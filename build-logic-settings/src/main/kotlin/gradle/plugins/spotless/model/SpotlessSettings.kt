package gradle.plugins.spotless.model

import com.diffplug.spotless.LineEnding
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.project.EnabledSettings
import gradle.plugins.spotless.FormatExtension
import gradle.plugins.spotless.SpotlessExtension
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class SpotlessSettings(
    override val lineEndings: LineEnding? = null,
    override val encoding: String? = null,
    override val ratchetFrom: String? = null,
    override val enforceCheck: Boolean? = null,
    override val predeclareDepsFromBuildscript: Boolean? = null,
    override val predeclareDeps: Boolean? = null,
    override val formats: LinkedHashSet<@Serializable(with = FormatExtensionKeyTransformingSerializer::class) FormatExtension>? = null,
    override val enabled: Boolean = true
) : SpotlessExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("spotless").id) {
            super.applyTo()
        }
}
