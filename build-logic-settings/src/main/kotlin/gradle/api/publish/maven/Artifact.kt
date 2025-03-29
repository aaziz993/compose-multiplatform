package gradle.api.publish.maven

import gradle.serialization.serializer.JsonKeyValueTransformingSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = ArtifactKeyValueTransformingSerializer::class)
internal data class Artifact(
    val source: String,
    val artifact: MavenArtifact? = null,
)

private object ArtifactKeyValueTransformingSerializer : JsonKeyValueTransformingSerializer<Artifact>(
    Artifact.generatedSerializer(),
    "source",
)
