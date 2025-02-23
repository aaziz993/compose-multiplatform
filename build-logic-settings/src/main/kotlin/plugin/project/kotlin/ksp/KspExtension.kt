package plugin.project.kotlin.ksp

import gradle.id
import gradle.ksp
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureKspExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("ksp").id) {
       projectProperties.plugins.ksp.let { ksp ->
            ksp(ksp::applyTo)
        }
    }

