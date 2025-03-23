package gradle.plugins.kotlin.room

import kotlinx.serialization.Serializable

@Serializable
internal data class SchemaDirectory(
    val matchName: String,
    val path: String
)
