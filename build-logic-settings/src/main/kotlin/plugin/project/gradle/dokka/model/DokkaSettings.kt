package plugin.project.gradle.dokka.model

import gradle.id
import gradle.libs
import gradle.model.gradle.dokka.DokkaExtension
import gradle.model.gradle.dokka.DokkaPluginParametersBaseSpec
import gradle.model.gradle.dokka.DokkaPluginParametersBaseSpecTransformingSerializer
import gradle.model.gradle.dokka.DokkaPublication
import gradle.model.gradle.dokka.DokkaPublicationTransformingSerializer
import gradle.model.gradle.dokka.DokkaSourceSetSpec
import gradle.model.gradle.dokka.DokkaSourceSetSpecTransformingSerializer
import gradle.model.gradle.dokka.WorkerIsolation
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.project.EnabledSettings

@Serializable
internal data class DokkaSettings(
    override val basePublicationsDirectory: String? = null,
    override val dokkaCacheDirectory: String? = null,
    override val moduleName: String? = null,
    override val moduleVersion: String? = null,
    override val modulePath: String? = null,
    override val sourceSetScopeDefault: String? = null,
    override val konanHome: String? = null,
    override val dokkaPublications: List<@Serializable(with = DokkaPublicationTransformingSerializer::class) DokkaPublication>? = null,
    override val dokkaSourceSets: List<@Serializable(with = DokkaSourceSetSpecTransformingSerializer::class) DokkaSourceSetSpec>? = null,
    override val pluginsConfiguration: List<@Serializable(with = DokkaPluginParametersBaseSpecTransformingSerializer::class) DokkaPluginParametersBaseSpec>? = null,
    override val dokkaEngineVersion: String? = null,
    override val dokkaGeneratorIsolation: WorkerIsolation? = null,
    override val enabled: Boolean = true,
) : DokkaExtension, EnabledSettings {

    context(Project)
    override fun applyTo() = pluginManager.withPlugin(settings.libs.plugins.plugin("dokka").id) {
        super.applyTo()
    }
}
