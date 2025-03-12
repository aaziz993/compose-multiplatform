package gradle.plugins.web.js.karakum

import gradle.karakum
import gradle.tryAssign
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
