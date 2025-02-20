package plugin.project.kotlinnative.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BinariesSettings(
    val framework: FrameworkSettings?=null
)
