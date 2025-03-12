package gradle.tasks

import gradle.tasks.copy.FileCopyDetails
import kotlinx.serialization.Serializable

@Serializable
internal data class FilesMatching(
    val patterns: List<String>,
    val fileCopyDetails: FileCopyDetails
)
