package gradle.plugins.kotlin.atomicfu

import gradle.accessors.atomicFU
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.trySet
import kotlinx.atomicfu.plugin.gradle.AtomicFUTransformTask
import org.gradle.api.Project

internal interface AtomicFUExtension {

    val dependenciesVersion: String?
    val transformJvm: Boolean?
    val jvmVariant: String?
    val verbose: Boolean?

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("atomicFU").id) {
            atomicFU::dependenciesVersion trySet dependenciesVersion
            atomicFU::transformJvm trySet transformJvm
            atomicFU::jvmVariant trySet jvmVariant
            atomicFU::verbose trySet verbose
        }
}
