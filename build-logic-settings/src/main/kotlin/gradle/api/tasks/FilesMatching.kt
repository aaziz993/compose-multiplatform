package gradle.api.tasks

import gradle.api.tasks.copy.FileCopyDetails
import kotlinx.serialization.Serializable

@Serializable
internal data class FilesMatching(
    val patterns: Set<String>,
    val fileCopyDetails: FileCopyDetails
)
