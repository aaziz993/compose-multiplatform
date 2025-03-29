package gradle.plugins.android

import gradle.serialization.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class MissingDimensionStrategy(
    val dimension: String,
    val requestedValues: List<String>
)

internal object MissingDimensionStrategyObjectTransformingSerializer : JsonObjectTransformingSerializer<MissingDimensionStrategy>(
    MissingDimensionStrategy.serializer(),
    "dimension",
)
