package gradle.plugins.dokka.model

import gradle.accessors.catalog.libs
import gradle.accessors.settings
import gradle.plugins.dokka.DokkaExtension
import gradle.plugins.dokka.DokkaPublication
import gradle.plugins.dokka.DokkaSourceSetSpec
import gradle.plugins.dokka.WorkerIsolation
import gradle.plugins.dokka.plugin.DokkaPluginParametersBaseSpec
import gradle.api.EnabledSettings
import kotlinx.serialization.Contextual
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
    override val dokkaPublications: LinkedHashSet<DokkaPublication>? = null,
    override val dokkaSourceSets: LinkedHashSet<DokkaSourceSetSpec>? = null,
    override val pluginsConfiguration: LinkedHashSet<DokkaPluginParametersBaseSpec<out @Contextual org.jetbrains.dokka.gradle.engine.plugins.DokkaPluginParametersBaseSpec>>? = null,
    override val dokkaEngineVersion: String? = null,
    override val dokkaGeneratorIsolation: WorkerIsolation? = null,
    override val enabled: Boolean = true,
    val dependenciesFromSubprojects: Boolean = true,
) : DokkaExtension, EnabledSettings {

    context(Project)
    override fun applyTo() = project.pluginManager.withPlugin(project.settings.libs.plugin("dokka").id) {
        super.applyTo()
    }
}
