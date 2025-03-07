package plugin.project.gradle.dokka.model

import gradle.id
import gradle.libs
import gradle.model.dokka.DokkaExtension
import gradle.model.dokka.DokkaPluginParametersBaseSpec
import gradle.model.dokka.DokkaPluginParametersBaseSpecTransformingSerializer
import gradle.model.dokka.DokkaPublication
import gradle.model.dokka.DokkaPublicationTransformingSerializer
import gradle.model.dokka.DokkaSourceSetSpec
import gradle.model.dokka.DokkaSourceSetSpecTransformingSerializer
import gradle.model.dokka.WorkerIsolation
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
