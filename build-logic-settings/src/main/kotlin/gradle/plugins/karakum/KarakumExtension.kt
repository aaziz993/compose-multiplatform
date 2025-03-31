package gradle.plugins.karakum

import gradle.accessors.karakum
import gradle.api.file.tryAssign
import gradle.api.provider.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class KarakumExtension(
    /**
     *  config file path relative to project
     */
    val configFile: String? = null,
    val extensionSource: String? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("io.github.sgrishchenko.karakum") {
            project.karakum.configFile tryAssign configFile?.let(project::file)
            project.karakum.extensionSource tryAssign extensionSource?.let(project.layout.projectDirectory::dir)?.asFileTree
        }
}

