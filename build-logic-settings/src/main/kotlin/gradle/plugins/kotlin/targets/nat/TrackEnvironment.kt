package gradle.plugins.kotlin.targets.nat

import gradle.serialization.serializer.JsonKeyValueTransformingSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = TrackEnvironmentKeyValueTransformingSerializer::class)
internal data class TrackEnvironment(
    val name: String,
    val tracked: Boolean = true,
)

private object TrackEnvironmentKeyValueTransformingSerializer : JsonKeyValueTransformingSerializer<TrackEnvironment>(
    TrackEnvironment.generatedSerializer(),
    "name",
)
