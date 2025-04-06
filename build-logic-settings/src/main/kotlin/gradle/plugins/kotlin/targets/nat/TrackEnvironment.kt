package gradle.plugins.kotlin.targets.nat

import klib.data.type.serialization.json.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = TrackEnvironmentObjectTransformingSerializer::class)
internal data class TrackEnvironment(
    val name: String,
    val tracked: Boolean = true,
)

private object TrackEnvironmentObjectTransformingSerializer : JsonObjectTransformingSerializer<TrackEnvironment>(
    TrackEnvironment.generatedSerializer(),
    "name",
)
