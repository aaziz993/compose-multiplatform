package plugin.project.kotlin.atomicfu

import gradle.atomicFU
import gradle.libs
import gradle.moduleProperties
import gradle.trySet
import kotlinx.atomicfu.plugin.gradle.AtomicFUGradlePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureAtomicFUPluginExtension() =
    pluginManager.withPlugin(libs.plugins.atomicfu.get().pluginId) {
        moduleProperties.settings.kotlin.atomicFU.let { atomicFU ->
            atomicFU(atomicFU::applyTo)
        }
    }
