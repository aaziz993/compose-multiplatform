package plugin.project.jvm.model

import kotlinx.serialization.Serializable

@Serializable
internal data class JvmSettings(
    val args: List<String>? = null,
)
