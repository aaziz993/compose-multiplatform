package gradle.project.sync

import kotlinx.serialization.Serializable

@Serializable
internal data class SyncFile(
    val from: String,
    val into: String,
    val resolution: SyncFileResolution = SyncFileResolution.IF_MODIFIED
)
