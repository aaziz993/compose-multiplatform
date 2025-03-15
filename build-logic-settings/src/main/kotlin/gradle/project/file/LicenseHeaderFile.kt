package gradle.project.file

import kotlinx.serialization.Serializable
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

@Serializable
internal data class LicenseHeaderFile(
    override val from: String,
    override val into: String,
    override val resolution: FileResolution = FileResolution.ABSENT,
) : ProjectFile {

    context(Project)
    override fun applyTo(name: String): TaskProvider<out DefaultTask> {
        super.applyTo(name).apply {
            configure {
                 {
                    val intoFile = outputs.files.singleFile

                    val text = intoFile.readText()

                    "/**\n${text.lines().joinToString("\n", " * ")}\n */"
                     text.lines().joinToString("\n", " # ")
                            "$<--\n$text\n -->"

                }
            }
        }
    }
}
