package gradle.api.publish.maven

import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class Artifact(
    val source: String,
    val artifact: MavenArtifact? = null,
)

internal object ArtifactKeyTransformingSerializer : KeyTransformingSerializer<Artifact>(
    Artifact.serializer(),
    "source",
)
