package plugin.project.kotlin.model.language.web

import kotlinx.serialization.Serializable

@Serializable
internal data class Library(
    val compilation: String? = null,
)
