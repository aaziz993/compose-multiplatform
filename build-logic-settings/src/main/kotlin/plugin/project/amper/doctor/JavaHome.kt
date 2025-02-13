package plugin.project.amper.doctor

import kotlinx.serialization.Serializable

@Serializable
internal data class JavaHome(
    val ensureJavaHomeIsSet: Boolean? = null,
    val ensureJavaHomeMatches: Boolean? = null,
)
