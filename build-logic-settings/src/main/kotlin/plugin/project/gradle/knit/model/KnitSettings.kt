package plugin.project.gradle.knit.model

import gradle.plugins.knit.KnitPluginExtension
import gradle.plugins.project.EnabledSettings
import kotlinx.serialization.Serializable

@Serializable
internal data class KnitSettings(
    override val siteRoot: String? = null,
    override val moduleRoots: List<String>? = null,
    override val moduleMarkers: List<String>? = null,
    override val moduleDocs: String? = null,
    override val files: List<String>? = null,
    override val rootDir: String? = null,
    override val dokkaMultiModuleRoot: String? = null,
    override val defaultLineSeparator: String? = null,
    override val enabled: Boolean = true
) : KnitPluginExtension, EnabledSettings
