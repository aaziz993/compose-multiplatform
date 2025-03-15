package gradle.project.file

import kotlinx.serialization.Serializable

@Serializable
internal data class LicenseHeaderFile(
    override val from: List<String>,
    override val into: String,
    override val resolution: FileResolution = FileResolution.ABSENT,
) : ProjectFile {

}
