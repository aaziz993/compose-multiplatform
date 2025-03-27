package gradle.plugins.knit.model

import gradle.accessors.knit
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.api.add
import gradle.api.plus
import gradle.ifTrue
import gradle.api.tryAdd
import gradle.plugins.knit.KnitPluginExtension
import gradle.plugins.project.EnabledSettings
import kotlin.collections.orEmpty
import kotlin.collections.toList
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class KnitSettings(
    override val siteRoot: String? = null,
    override val moduleRoots: List<String>? = null,
    override val setModuleRoots: List<String>? = null,
    override val moduleMarkers: List<String>? = null,
    override val setModuleMarkers: List<String>? = null,
    override val moduleDocs: String? = null,
    override val files: Set<String>? = null,
    override val setFiles: Set<String>? = null,
    override val rootDir: String? = null,
    override val dokkaMultiModuleRoot: String? = null,
    override val defaultLineSeparator: String? = null,
    override val enabled: Boolean = true,
    val moduleRootsFromIncludes: Boolean = true,
) : KnitPluginExtension, EnabledSettings {

    context(Project)
    override fun applyTo() {
        super.applyTo()

        moduleRootsFromIncludes.ifTrue {
            project.knit::moduleRoots + project.settings.projectProperties.includesAsPaths.orEmpty() + listOf(".")
        }
    }
}
