package gradle.project.file

import kotlinx.serialization.Serializable

@Serializable
internal data class LicenseHeaderFile(
    override val resolution: FileResolution = FileResolution.ABSENT,
) : ProjectFile {
    over
}
