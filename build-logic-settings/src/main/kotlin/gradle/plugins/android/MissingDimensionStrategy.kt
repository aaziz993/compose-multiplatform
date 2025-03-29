package gradle.plugins.android

import gradle.serialization.serializer.JsonKeyValueTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class MissingDimensionStrategy(
    val dimension: String,
    val requestedValues: List<String>
)

internal object MissingDimensionStrategyKeyValueTransformingSerializer : JsonKeyValueTransformingSerializer<MissingDimensionStrategy>(
    MissingDimensionStrategy.serializer(),
    "dimension",
)
