package plugin.project.kotlin.kmp.model.web

import kotlinx.serialization.Serializable

@Serializable
internal data class Library(
    val compilation: String? = null,
)
