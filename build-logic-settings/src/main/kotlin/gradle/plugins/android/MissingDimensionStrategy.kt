package gradle.plugins.android

import klib.data.type.serialization.serializer.MapTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class MissingDimensionStrategy(
    val dimension: String,
    val requestedValues: List<String>
)

internal object MissingDimensionStrategyMapTransformingSerializer : MapTransformingSerializer<MissingDimensionStrategy>(
    MissingDimensionStrategy.serializer(),
    "dimension",
)
