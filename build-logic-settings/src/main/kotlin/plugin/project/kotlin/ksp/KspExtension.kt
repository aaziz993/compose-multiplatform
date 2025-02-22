package plugin.project.kotlin.ksp

import gradle.ksp
import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureKspExtension() =
    pluginManager.withPlugin(libs.plugins.ksp.get().pluginId) {
       projectProperties.plugins.ksp.let { ksp ->
            ksp(ksp::applyTo)
        }
    }

