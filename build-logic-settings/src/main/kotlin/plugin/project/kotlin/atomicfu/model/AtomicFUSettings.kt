package plugin.project.kotlin.atomicfu.model

import gradle.trySet
import kotlinx.atomicfu.plugin.gradle.AtomicFUPluginExtension
import kotlinx.serialization.Serializable

@Serializable
internal data class AtomicFUSettings(
    override val dependenciesVersion: String? = null,
    override val transformJvm: Boolean? = null,
    override val jvmVariant: String? = null,
    override val verbose: Boolean? = null,
    val enabled: Boolean = true
) : AtomicFUExtension {

    fun applyTo(extension: AtomicFUPluginExtension) {
        extension::dependenciesVersion trySet dependenciesVersion
        extension::transformJvm trySet transformJvm
        extension::jvmVariant trySet jvmVariant
        extension::verbose trySet verbose
    }
}
