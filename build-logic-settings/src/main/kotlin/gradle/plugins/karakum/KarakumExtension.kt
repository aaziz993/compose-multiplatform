package gradle.plugins.karakum

import gradle.accessors.id
import gradle.accessors.karakum
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.tryAssign
import org.gradle.api.Project

internal interface KarakumExtension {

    /**
     *  config file path relative to project
     */
    val configFile: String?
    val extensionSource: String?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("karakum").id) {
            project.karakum.configFile tryAssign configFile?.let(project::file)
            project.karakum.extensionSource tryAssign extensionSource?.let(project.layout.projectDirectory::dir)?.asFileTree
        }
}

