package gradle.plugins.kmp.web

import kotlinx.serialization.Serializable

@Serializable
internal data class Executable(
    val compilation: String? = null,
)
