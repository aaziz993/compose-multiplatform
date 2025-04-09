package gradle.api.publish.maven

import klib.data.type.serialization.serializer.MapTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = ArtifactMapTransformingSerializer::class)
internal data class Artifact(
    val source: String,
    val artifact: MavenArtifact? = null,
)

private object ArtifactMapTransformingSerializer : MapTransformingSerializer<Artifact>(
    Artifact.generatedSerializer(),
    "source",
)
