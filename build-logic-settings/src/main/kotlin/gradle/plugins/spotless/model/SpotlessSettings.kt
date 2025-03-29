package gradle.plugins.spotless.model

import com.diffplug.spotless.LineEnding
import gradle.accessors.catalog.libs
import gradle.accessors.settings
import gradle.plugins.project.EnabledSettings
import gradle.plugins.spotless.FormatExtension
import gradle.plugins.spotless.SpotlessExtension
import kotlinx.serialization.Contextual
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
    override val formats: LinkedHashSet<FormatExtension<out @Contextual com.diffplug.gradle.spotless.FormatExtension>>? = null,
    override val enabled: Boolean = true
) : SpotlessExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("spotless").id) {
            super.applyTo()
        }
}
