package plugin.project.kotlin.model.web

import kotlinx.serialization.Serializable

@Serializable
internal data class Executable(
    val compilation: String?=null,
)
