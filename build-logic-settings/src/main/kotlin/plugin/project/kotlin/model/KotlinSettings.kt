package plugin.project.kotlin.model

import kotlinx.serialization.Serializable
import plugin.project.kotlin.atomicfu.model.AtomicFUSettings
import plugin.project.kotlin.ksp.model.KspSettings

@Serializable
internal data class KotlinSettings(
    val ksp: KspSettings = KspSettings(),
    val atomicFU: AtomicFUSettings = AtomicFUSettings(),
)
