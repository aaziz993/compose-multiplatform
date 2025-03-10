package gradle.model.repository

import kotlinx.serialization.Serializable

@Serializable
internal data class Version(
    val group: String,
    val moduleName: String,
    val version: String
)
