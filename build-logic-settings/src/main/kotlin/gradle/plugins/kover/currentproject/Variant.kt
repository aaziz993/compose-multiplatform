package gradle.plugins.kover.currentproject

import kotlinx.serialization.Serializable

@Serializable
internal data class Variant(
    val names: List<String>,
    val optional: Boolean = false,
)
