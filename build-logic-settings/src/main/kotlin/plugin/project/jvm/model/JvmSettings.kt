package plugin.project.jvm.model

import kotlinx.serialization.Serializable

@Serializable
internal data class JvmSettings(
    val compileSdk: Int = 23,
    val testSdk: Int = 23,
)
