package plugin.project.kotlin.noarg.model

import kotlinx.serialization.Serializable
import plugin.project.kotlin.allopen.model.AllOpenExtension

@Serializable
internal data class NoArgSettings(
    override val myAnnotations: List<String>? = null,
    override val myPresets: List<String>? = null,
    val enabled: Boolean = true,
) : NoArgExtension
