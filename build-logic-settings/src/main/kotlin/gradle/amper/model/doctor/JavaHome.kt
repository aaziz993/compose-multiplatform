package gradle.amper.model.doctor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class JavaHome(
    val ensureJavaHomeIsSet: Boolean? = null,
    val ensureJavaHomeMatches: Boolean? = null,
)