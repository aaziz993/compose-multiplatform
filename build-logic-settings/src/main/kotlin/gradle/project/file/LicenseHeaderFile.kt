package gradle.project.file

import kotlinx.serialization.Serializable
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

@Serializable
internal data class LicenseHeaderFile(
    override val from: List<String>,
    override val into: String,
    override val resolution: FileResolution = FileResolution.ABSENT,
) : ProjectFile {


}
