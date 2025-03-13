package gradle.api.publish.maven

import kotlinx.serialization.Serializable

@Serializable
internal data class Artifact(
    val source: String,
    val artifact: MavenArtifact
)
