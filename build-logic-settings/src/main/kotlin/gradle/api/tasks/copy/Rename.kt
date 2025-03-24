package gradle.api.tasks.copy

import kotlinx.serialization.Serializable

@Serializable
internal data class Rename(
    val sourceRegEx: String,
    val replaceWith: String
)
