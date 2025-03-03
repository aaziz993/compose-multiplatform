package plugin.project.gradle.model

import kotlinx.serialization.Serializable

@Serializable
internal data class FilesMatching(
    val patterns: List<String>,
    val fileCopyDetails: FileCopyDetails
)
