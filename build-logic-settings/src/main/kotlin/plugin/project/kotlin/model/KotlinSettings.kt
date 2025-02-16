package plugin.project.kotlin.model

import kotlinx.serialization.Serializable
import plugin.project.gradle.atomicfu.model.AtomicFUExtensionSettings

@Serializable
internal data class KotlinSettings(
    val atomicFU: AtomicFUExtensionSettings = AtomicFUExtensionSettings()
)
