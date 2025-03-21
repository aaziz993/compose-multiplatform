package gradle.api.tasks.copy

import kotlinx.serialization.Serializable

@Serializable
internal data class From(
    val sourcePath: String,
    val copySpec: CopySpecImpl,
)
