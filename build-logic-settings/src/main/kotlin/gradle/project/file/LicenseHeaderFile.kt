package gradle.project.file

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.configure

@Serializable
internal data class LicenseHeaderFile(
    val source: String,
    override val resolution: FileResolution = FileResolution.ABSENT,
) : ProjectFile {

    @Transient
    override val from: List<String> = listOf(source)

    @Transient
    override val into: String = "licenses/LICENSE_HEADER"

    context(Project)
    override fun applyTo(name: String): List<TaskProvider<out DefaultTask>> =
        super.applyTo(name).also { tasks ->
            tasks.single().configure {
                doLast {
                    val intoFile = file(into)
                    if (intoFile.exists()) {
                        val text = intoFile.readText()

                        file("${into}_SLASHED").writeText("/**\n${text.lines().joinToString("\n", " * ")}\n */")
                        file("${into}_SHARPED").writeText(text.lines().joinToString("\n", " # "))
                        file("${into}_TAGGED").writeText("$<--\n$text\n -->")

                        intoFile.delete()
                    }
                }
            }
        }
}
