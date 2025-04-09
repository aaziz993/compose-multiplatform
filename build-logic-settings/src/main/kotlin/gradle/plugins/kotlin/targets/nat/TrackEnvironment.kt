package gradle.plugins.kotlin.targets.nat

import klib.data.type.serialization.serializer.MapTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = TrackEnvironmentMapTransformingSerializer::class)
internal data class TrackEnvironment(
    val name: String,
    val tracked: Boolean = true,
)

private object TrackEnvironmentMapTransformingSerializer : MapTransformingSerializer<TrackEnvironment>(
    TrackEnvironment.generatedSerializer(),
    "name",
)
