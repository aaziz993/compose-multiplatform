package gradle.plugins.karakum

import gradle.accessors.id
import gradle.accessors.karakum
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.tryAssign
import gradle.serialization.decodeMapFromString
import java.io.File
import kotlinx.serialization.json.Json
import org.gradle.api.Project

internal interface KarakumExtension {

    /**
     *  config file path relative to project
     */
    val configFile: String?
    val extensionSource: String?

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("karakum").id) {
            karakum.configFile tryAssign configFile?.let(::file)
            karakum.extensionSource tryAssign extensionSource?.let(layout.projectDirectory::dir)?.asFileTree
        }
}

