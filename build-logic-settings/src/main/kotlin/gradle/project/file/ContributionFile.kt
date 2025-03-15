package gradle.project.file

import kotlinx.serialization.Serializable

@Serializable
internal data class ContributionFile(
    override val from: List<String>,
    override val resolution: FileResolution = FileResolution.ABSENT,
) : ProjectFile {

    override val into: String
        get() = "./"
}
