package gradle.project.sync

internal enum class SyncFileResolution {
        IF_MODIFIED,
        IF_NEWER,
        OVERRIDE
    }
