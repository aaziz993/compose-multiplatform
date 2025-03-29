package gradle.api.publish.maven

import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = ArtifactKeyTransformingSerializer::class)
internal data class Artifact(
    val source: String,
    val artifact: MavenArtifact? = null,
)

private object ArtifactKeyTransformingSerializer : KeyTransformingSerializer<Artifact>(
    Artifact.generatedSerializer(),
    "source",
)
