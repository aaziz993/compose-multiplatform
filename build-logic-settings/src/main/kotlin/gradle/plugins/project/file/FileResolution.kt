package gradle.plugins.project.file

internal enum class FileResolution {
    ABSENT,
    OVERRIDE,
    MODIFIED,
    NEWER
}
