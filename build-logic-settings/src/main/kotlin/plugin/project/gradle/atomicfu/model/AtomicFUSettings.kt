package plugin.project.gradle.atomicfu.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AtomicFUSettings(
    override val dependenciesVersion: String? = null,
    override val transformJvm: Boolean? = null,
    override val jvmVariant: String? = null,
    override val verbose: Boolean? = null,
) : AtomicFUExtension
