package gradle.api.publish.maven

import gradle.serialization.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = ArtifactObjectTransformingSerializer::class)
internal data class Artifact(
    val source: String,
    val artifact: MavenArtifact? = null,
)

private object ArtifactObjectTransformingSerializer : JsonObjectTransformingSerializer<Artifact>(
    Artifact.generatedSerializer(),
    "source",
)
