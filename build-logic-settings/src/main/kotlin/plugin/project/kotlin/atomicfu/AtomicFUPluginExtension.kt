package plugin.project.kotlin.atomicfu

import gradle.atomicFU
import gradle.moduleProperties
import gradle.trySet
import kotlinx.atomicfu.plugin.gradle.AtomicFUGradlePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureAtomicFUPluginExtension() =
    plugins.withType<AtomicFUGradlePlugin> {
        moduleProperties.settings.kotlin.atomicFU.let { atomicFU ->
            atomicFU {
                ::dependenciesVersion trySet atomicFU.dependenciesVersion
                ::transformJvm trySet atomicFU.transformJvm
                ::jvmVariant trySet atomicFU.jvmVariant
                ::verbose trySet atomicFU.verbose
            }
        }
    }
