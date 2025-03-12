package gradle.tasks.copy

import kotlinx.serialization.Serializable

@Serializable
internal data class IntoSpec(
    val destPath: String,
    val copySpec: CopySpecImpl,
)
