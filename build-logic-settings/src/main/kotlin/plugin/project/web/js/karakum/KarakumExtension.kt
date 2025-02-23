package plugin.project.web.js.karakum

import gradle.id
import gradle.karakum
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import gradle.tryAssign
import org.gradle.api.Project

internal fun Project.configureKarakumExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("karakum").id) {
       projectProperties.settings.web.karakum.let { karakum ->
            karakum {
                configFile tryAssign karakum.configFile?.let(::file)
                extensionSource tryAssign karakum.extensionSource?.let(layout.projectDirectory::dir)?.asFileTree
            }
        }
    }
