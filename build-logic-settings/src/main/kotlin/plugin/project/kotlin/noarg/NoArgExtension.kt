package plugin.project.kotlin.noarg

import gradle.id
import gradle.libs
import gradle.noArg
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureNoArgExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("noarg").id) {
       projectProperties.plugins.noArg.let { noArg ->
            noArg(noArg::applyTo)
        }
    }
