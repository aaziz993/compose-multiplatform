package plugin.project.gradle.atomicfu.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AtomicFUExtensionSettings(
    val dependenciesVersion: String? = null,
    val transformJvm: Boolean? = null,
    val jvmVariant: String? = null,
    val verbose: Boolean? = null,
)
