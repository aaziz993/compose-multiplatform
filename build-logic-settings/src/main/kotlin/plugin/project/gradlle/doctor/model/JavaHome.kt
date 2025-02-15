package plugin.project.gradlle.doctor.model

import kotlinx.serialization.Serializable

@Serializable
internal data class JavaHome(
    val ensureJavaHomeIsSet: Boolean? = null,
    val ensureJavaHomeMatches: Boolean? = null,
)
