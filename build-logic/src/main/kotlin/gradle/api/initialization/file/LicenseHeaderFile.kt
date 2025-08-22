package gradle.api.initialization.file

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

@Serializable
public data class LicenseHeaderFile(
    val source: String,
    override val resolution: FileResolution = FileResolution.ABSENT
) : ProjectFile() {

    @Transient
    override val from: List<String> = listOf(source)

    @Transient
    override val into: String = "licenses/LICENSE_HEADER"

    context(project: Project)
    override fun applyTo(receiver: String): List<TaskProvider<out DefaultTask>> =
        super.applyTo(receiver).onEach { task ->
            task.configure {
                val intoFile = project.file(into)

                var previousTemplateText: String? = null

                doFirst {
                    // Remember previous template
                    if (intoFile.exists()) {
                        previousTemplateText = intoFile.readText()
                    }
                }

                doLast {
                    if (intoFile.exists()) {
                        val templateText = intoFile.readText()

                        if (previousTemplateText == null || templateText != previousTemplateText) {
                            project.file("${into}_SLASHED").writeText(
                                "/**\n${
                                    templateText.lines().joinToString("\n", " * ")
                                }\n */",
                            )
                            project.file("${into}_SHARPED").writeText(
                                templateText.lines().joinToString("\n", " # "),
                            )
                            project.file("${into}_TAGGED").writeText("$<--\n$templateText\n -->")
                        }
                    }
                }
            }
        }
}
