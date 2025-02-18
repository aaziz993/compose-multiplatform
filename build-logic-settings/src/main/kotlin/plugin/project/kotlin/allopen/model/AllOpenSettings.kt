package plugin.project.kotlin.allopen.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AllOpenSettings(
    override val myAnnotations: List<String>? = null,
    override val myPresets: List<String>? = null,
    val enabled: Boolean = true,
) : AllOpenExtension
