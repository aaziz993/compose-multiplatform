package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal data class FormatAnnotationsConfig(
    val addedTypeAnnotations: List<String>? = null,
    val removedTypeAnnotations: List<String>? = null
)
