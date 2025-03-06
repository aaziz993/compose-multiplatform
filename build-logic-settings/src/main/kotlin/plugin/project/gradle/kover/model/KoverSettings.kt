package plugin.project.gradle.kover.model

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.model.EnabledSettings

@Serializable
internal data class KoverSettings(
    override val useJacoco: Boolean? = null,
    override val jacocoVersion: String? = null,
    override val currentProject: KoverCurrentProjectVariantsConfig? = null,
    override val reports: KoverReportsConfig? = null,
    override val enabled: Boolean = true,
) : KoverExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("kover").id) {
            super.applyTo()
        }
}
