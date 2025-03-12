package gradle.plugins.kmp.nat

import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class TrackEnvironment(
    val name: String,
    val tracked: Boolean = true,
)

internal object TrackEnvironmentTransformingSerializer : KeyTransformingSerializer<TrackEnvironment>(
    TrackEnvironment.serializer(),
    "name",
)
