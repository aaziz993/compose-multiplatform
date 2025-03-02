package plugin.project.gradle.dokka.model

import gradle.dokka
import gradle.id
import gradle.libraries
import gradle.library
import gradle.libs
import gradle.module
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import plugin.project.model.EnabledSettings

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
    override val enabled: Boolean = true,
    val versioning: Boolean = true,
) : DokkaExtension, EnabledSettings {

    context(Project)
    fun applyTo() = pluginManager.withPlugin(settings.libs.plugins.plugin("dokka").id) {
        super.applyTo(dokka)

        if (versioning) {
            val dokkaPlugin by configurations

            dependencies {
                dokkaPlugin(settings.libs.libraries.library("dokka.versioning").module)
            }
        }
    }
}
