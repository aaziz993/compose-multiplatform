package gradle.plugins.kotlin.targets.nat

import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = TrackEnvironmentKeyTransformingSerializer::class)
internal data class TrackEnvironment(
    val name: String,
    val tracked: Boolean = true,
)

private object TrackEnvironmentKeyTransformingSerializer : KeyTransformingSerializer<TrackEnvironment>(
    TrackEnvironment.generatedSerializer(),
    "name",
)
