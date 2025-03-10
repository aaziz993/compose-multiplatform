package gradle.model.gradle.publish.publication

import gradle.model.gradle.publish.publication.MavenArtifact
import kotlinx.serialization.Serializable

@Serializable
internal data class Artifact(
    val source: String,
    val artifact: MavenArtifact
)
