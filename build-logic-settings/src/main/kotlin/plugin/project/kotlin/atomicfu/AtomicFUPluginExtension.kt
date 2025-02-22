package plugin.project.kotlin.atomicfu

import gradle.atomicFU
import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureAtomicFUPluginExtension() =
    pluginManager.withPlugin(libs.plugins.atomicfu.get().pluginId) {
       projectProperties.plugins.atomicFU.let { atomicFU ->
            atomicFU(atomicFU::applyTo)
        }
    }
