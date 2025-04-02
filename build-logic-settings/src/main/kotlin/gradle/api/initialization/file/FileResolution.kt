package gradle.api.initialization.file

internal enum class FileResolution {
    ABSENT,
    OVERRIDE,
    MODIFIED,
    NEWER
}
