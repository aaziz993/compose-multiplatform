package gradle.plugins.kotlin.atomicfu

import gradle.accessors.atomicFU
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.trySet
import org.gradle.api.Project

internal interface AtomicFUExtension {

    val dependenciesVersion: String?
    val transformJvm: Boolean?
    val jvmVariant: String?
    val verbose: Boolean?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("atomicfu").id) {
            project.atomicFU::dependenciesVersion trySet dependenciesVersion
            project.atomicFU::transformJvm trySet transformJvm
            project.atomicFU::jvmVariant trySet jvmVariant
            project.atomicFU::verbose trySet verbose
        }
}
