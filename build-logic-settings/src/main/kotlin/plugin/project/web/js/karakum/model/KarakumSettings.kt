package plugin.project.web.js.karakum.model

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class KarakumSettings(
    override val configFile: String? = null,
    override val extensionSource: String? = null,
    val task: KarakumGenerate = KarakumGenerate(),
    val enabled: Boolean = true
) : KarakumExtension {

    context(Project)
    fun applyTo(extension: io.github.sgrishchenko.karakum.gradle.plugin.KarakumExtension) {
        extension.configFile tryAssign configFile?.let(::file)
        extension.extensionSource tryAssign extensionSource?.let(layout.projectDirectory::dir)?.asFileTree
    }
}
