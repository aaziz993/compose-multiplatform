package gradle.api.initialization.file

import gradle.api.initialization.settingsProperties
import gradle.api.project.settings
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

@Serializable
public data class CodeOfConductFile(
    val source: String,
    override val resolution: FileResolution = FileResolution.ABSENT,
    val emailPlaceholder: String,
    val email: String? = null
) : ProjectFile() {

    @Transient
    override val from: List<String> = listOf(source)

    @Transient
    override val into: String = "CODE_OF_CONDUCT.md"

    @Transient
    override val replace: MutableMap<String, String> = mutableMapOf()

    context(project: Project)
    override fun applyTo(receiver: String): List<TaskProvider<out DefaultTask>> {
//        (email ?: project.settings.settingsProperties.developer?.email)?.let { email ->
//            replace[emailPlaceholder] = email
//        }

        return super.applyTo(receiver)
    }
}
