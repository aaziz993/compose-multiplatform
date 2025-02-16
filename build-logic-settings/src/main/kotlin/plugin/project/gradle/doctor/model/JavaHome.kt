package plugin.project.gradle.doctor.model

import kotlinx.serialization.Serializable

@Serializable
internal data class JavaHome(
    val ensureJavaHomeIsSet: Boolean? = null,
    val ensureJavaHomeMatches: Boolean? = null,
)
