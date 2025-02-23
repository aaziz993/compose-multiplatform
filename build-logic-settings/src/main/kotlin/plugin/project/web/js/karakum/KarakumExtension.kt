package plugin.project.web.js.karakum

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.karakum
import gradle.projectProperties
import gradle.settings
import gradle.tryAssign
import io.github.sgrishchenko.karakum.gradle.plugin.KarakumPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureKarakumExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("karakum").id) {
       projectProperties.settings.web.karakum.let { karakum ->
            karakum {
                configFile tryAssign karakum.configFile?.let(::file)
                extensionSource tryAssign karakum.extensionSource?.let(layout.projectDirectory::dir)?.asFileTree
            }
        }
    }
