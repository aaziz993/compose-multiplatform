package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal class CleanthatJavaConfig(
    val groupArtifact: String? = null,
    val version: String? = null,
    val sourceJdk: String? = null,
    val mutators: List<String?>? = null,
    val excludedMutators: List<String?>? = null,
    val includeDraft: Boolean? = null
)
