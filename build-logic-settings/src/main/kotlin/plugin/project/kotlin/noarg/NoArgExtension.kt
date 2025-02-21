package plugin.project.kotlin.noarg

import gradle.allOpen
import gradle.libs
import gradle.moduleProperties
import gradle.noArg
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.noarg.gradle.NoArgGradleSubplugin

internal fun Project.configureNoArgExtension() =
    pluginManager.withPlugin(libs.plugins.noarg.get().pluginId) {
        moduleProperties.settings.kotlin.noArg.let { noArg ->
            noArg(noArg::applyTo)
        }
    }
