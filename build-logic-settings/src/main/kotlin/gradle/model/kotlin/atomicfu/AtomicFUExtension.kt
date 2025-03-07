package gradle.model.kotlin.atomicfu

import gradle.atomicFU
import gradle.trySet
import org.gradle.api.Project

internal interface AtomicFUExtension {

    val dependenciesVersion: String?
    val transformJvm: Boolean?
    val jvmVariant: String?
    val verbose: Boolean?

    context(Project)
    fun applyTo() {
        atomicFU::dependenciesVersion trySet dependenciesVersion
        atomicFU::transformJvm trySet transformJvm
        atomicFU::jvmVariant trySet jvmVariant
        atomicFU::verbose trySet verbose
    }
}
