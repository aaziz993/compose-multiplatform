package gradle.api.initialization.file

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.Project

@Serializable
public data class CodeOfConductFile(
    val source: String,
    override val resolution: FileResolution = FileResolution.ABSENT,
    val email: String? = null,
    val emailPlaceholder: String
) : ProjectFile() {

    @Transient
    override val from: List<String> = listOf(source)

    @Transient
    override val into: String = "CODE_OF_CONDUCT.md"

    @Transient
    override val replace: MutableMap<String, String> = mutableMapOf()

    context(project: Project)
    override fun applyTo(receiver: String): List<TaskProvider<out DefaultTask>> {
        email?.let { email -> replace[emailPlaceholder] = email }

        return super.applyTo(receiver)
    }
}
