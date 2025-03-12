package plugins.dokka.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.plugins.dokka.DokkaExtension
import gradle.plugins.dokka.DokkaPluginParametersBaseSpec
import gradle.plugins.dokka.DokkaPluginParametersBaseSpecTransformingSerializer
import gradle.plugins.dokka.DokkaPublication
import gradle.plugins.dokka.DokkaPublicationTransformingSerializer
import gradle.plugins.dokka.DokkaSourceSetSpec
import gradle.plugins.dokka.DokkaSourceSetSpecTransformingSerializer
import gradle.plugins.dokka.WorkerIsolation
import gradle.plugins.project.EnabledSettings
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

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
