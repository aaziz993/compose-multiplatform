package gradle.plugins.kover.model

import gradle.accessors.catalog.libs

import gradle.accessors.settings
import gradle.plugins.kover.KoverExtension
import gradle.plugins.kover.currentproject.KoverCurrentProjectVariantsConfig
import gradle.plugins.kover.reports.KoverReportsConfig
import gradle.api.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class KoverSettings(
    override val useJacoco: Boolean? = null,
    override val jacocoVersion: String? = null,
    override val currentProject: KoverCurrentProjectVariantsConfig? = null,
    override val reports: KoverReportsConfig? = null,
    override val enabled: Boolean = true,
    val dependenciesFromSubprojects: Boolean = true,
) : KoverExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("kover").id) {
            super.applyTo()
        }
}
