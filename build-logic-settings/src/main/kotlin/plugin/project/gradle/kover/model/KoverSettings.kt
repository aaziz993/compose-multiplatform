package plugin.project.gradle.kover.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.plugins.kover.KoverCurrentProjectVariantsConfig
import gradle.plugins.kover.KoverExtension
import gradle.plugins.kover.KoverReportsConfig
import gradle.plugins.project.EnabledSettings
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

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
