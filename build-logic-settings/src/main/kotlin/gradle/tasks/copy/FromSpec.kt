package gradle.tasks.copy

import kotlinx.serialization.Serializable

@Serializable
internal data class FromSpec(
    val sourcePath: String,
    val copySpec: CopySpecImpl,
)
