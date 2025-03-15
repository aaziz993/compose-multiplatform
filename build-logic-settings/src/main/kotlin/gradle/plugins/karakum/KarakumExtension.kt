package gradle.plugins.karakum

import gradle.accessors.karakum
import gradle.api.tryAssign
import org.gradle.api.Project

internal interface KarakumExtension {

    /**
     *  config file path relative to project
     */
    val configFile: String?
    val extensionSource: String?

    context(Project)
    fun applyTo() {
        karakum.configFile tryAssign configFile?.let(::file)
        karakum.extensionSource tryAssign extensionSource?.let(layout.projectDirectory::dir)?.asFileTree
    }
}
