package plugin.project.kotlinnative.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Binaries(
    val framework: Framework?=null
)
