package plugin.project.gradle.dokka.model

import kotlinx.serialization.Serializable

@Serializable
internal data class DokkaModuleSettings(
    override val basePublicationsDirectory: String? = null,
    override val dokkaCacheDirectory: String? = null,
    override val moduleName: String? = null,
    override val moduleVersion: String? = null,
    override val modulePath: String? = null,
    override val sourceSetScopeDefault: String? = null,
    override val konanHome: String? = null,
    override val dokkaPublications: List<DokkaPublication>? = null,
    override val dokkaSourceSets: List<DokkaSourceSetSpec>? = null,
    override val dokkaEngineVersion: String? = null,
    val task: DokkaMultiModuleTask? = null
) : DokkaExtension
