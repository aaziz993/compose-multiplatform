package plugin.project.kotlin.model

import kotlinx.serialization.Serializable
import plugin.project.gradle.atomicfu.model.AtomicFUSettings

@Serializable
internal data class KotlinSettings(
    val atomicFU: AtomicFUSettings = AtomicFUSettings()
)
