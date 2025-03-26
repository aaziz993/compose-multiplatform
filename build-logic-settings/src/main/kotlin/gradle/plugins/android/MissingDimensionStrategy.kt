package gradle.plugins.android

import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class MissingDimensionStrategy(
    val dimension: String,
    val requestedValues: List<String>
)

internal object MissingDimensionStrategyKeyTransformingSerializer : KeyTransformingSerializer<MissingDimensionStrategy>(
    MissingDimensionStrategy.serializer(),
    "dimension",
)
