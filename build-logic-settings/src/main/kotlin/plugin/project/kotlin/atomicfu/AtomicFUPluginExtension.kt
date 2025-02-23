package plugin.project.kotlin.atomicfu

import gradle.atomicFU
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureAtomicFUPluginExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("atomicfu").id) {
       projectProperties.plugins.atomicFU.let { atomicFU ->
            atomicFU(atomicFU::applyTo)
        }
    }
