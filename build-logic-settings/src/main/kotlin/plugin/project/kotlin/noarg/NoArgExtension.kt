package plugin.project.kotlin.noarg

import gradle.libs
import gradle.projectProperties
import gradle.noArg
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureNoArgExtension() =
    pluginManager.withPlugin(libs.plugins.noarg.get().pluginId) {
       projectProperties.plugins.noArg.let { noArg ->
            noArg(noArg::applyTo)
        }
    }
