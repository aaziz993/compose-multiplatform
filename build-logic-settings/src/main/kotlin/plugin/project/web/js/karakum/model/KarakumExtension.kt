package plugin.project.web.js.karakum.model

import org.gradle.api.file.FileTree
import org.gradle.api.provider.Property

internal interface KarakumExtension {

    /**
     *  config file path relative to project
     */
    val configFile: String?
    val extensionSource: String?
}
