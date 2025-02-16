package plugin.project.gradle.atomicfu

import gradle.amperModuleExtraProperties
import gradle.atomicFU
import gradle.trySet
import kotlinx.atomicfu.plugin.gradle.AtomicFUGradlePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureAtomicFUPluginExtension() =
    plugins.withType<AtomicFUGradlePlugin> {
        amperModuleExtraProperties.settings.kotlin.atomicFU.let { atomicFU ->
            atomicFU {
                ::dependenciesVersion trySet atomicFU.dependenciesVersion
                ::transformJvm trySet atomicFU.transformJvm
                ::jvmVariant trySet atomicFU.jvmVariant
                ::verbose trySet atomicFU.verbose
            }
        }
    }
