package gradle.plugins.kotlin.atomicfu

import gradle.accessors.atomicFU
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class AtomicFuExtension(
    val dependenciesVersion: String? = null,
    val transformJvm: Boolean? = null,
    val jvmVariant: String? = null,
    val verbose: Boolean? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.kotlinx.atomicfu") {
            project.atomicFU::dependenciesVersion trySet dependenciesVersion
            project.atomicFU::transformJvm trySet transformJvm
            project.atomicFU::jvmVariant trySet jvmVariant
            project.atomicFU::verbose trySet verbose
        }
}
