package plugin.project.kotlin.model.web

import kotlinx.serialization.Serializable

@Serializable
internal data class Library(
    val compilation: String? = null,
)
