package plugin.project.kotlinnative.model

import kotlinx.serialization.Serializable

@Serializable
internal data class NativeSettings(
    val binaries: BinariesSettings? = null,
)
