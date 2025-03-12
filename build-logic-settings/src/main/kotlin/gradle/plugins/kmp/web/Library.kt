package gradle.plugins.kmp.web

import kotlinx.serialization.Serializable

@Serializable
internal data class Library(
    val compilation: String? = null,
)
