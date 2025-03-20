package gradle.plugins.android.flavor

import kotlinx.serialization.Serializable

@Serializable
internal data class FlavorDimensionAndValues(
    val dimension: String,
    val values: List<String>
)
