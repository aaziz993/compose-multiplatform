package plugin.project.kotlin.atomicfu.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AtomicFUSettings(
    override val dependenciesVersion: String? = null,
    override val transformJvm: Boolean? = null,
    override val jvmVariant: String? = null,
    override val verbose: Boolean? = null,
    val enabled: Boolean = true
) : AtomicFUExtension
