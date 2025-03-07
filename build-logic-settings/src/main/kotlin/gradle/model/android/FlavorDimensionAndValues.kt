package gradle.model.android

import kotlinx.serialization.Serializable

@Serializable
internal data class FlavorDimensionAndValues(
    val dimension: String,
    val values: List<String>
)
