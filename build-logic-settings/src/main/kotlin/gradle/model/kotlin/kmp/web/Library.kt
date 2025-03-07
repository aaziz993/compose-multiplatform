package gradle.model.kotlin.kmp.web

import kotlinx.serialization.Serializable

@Serializable
internal data class Library(
    val compilation: String? = null,
)
