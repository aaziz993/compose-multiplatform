package gradle.plugins.kotlin.targets.nat

import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class TrackEnvironment(
    val name: String,
    val tracked: Boolean = true,
)

internal object TrackEnvironmentKeyTransformingSerializer : KeyTransformingSerializer<TrackEnvironment>(
    TrackEnvironment.serializer(),
    "name",
)
