package gradle.project.file

import kotlinx.serialization.Serializable

@Serializable
internal data class RootProjectFile(
    override val from: String,
    override val resolution: FileResolution = FileResolution.ABSENT,
    override val replace: Map<String, String> = emptyMap()
) : ProjectFile {

    override val into: String
        get() = "./"
}
