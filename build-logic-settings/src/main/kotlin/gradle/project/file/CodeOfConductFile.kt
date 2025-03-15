package gradle.project.file

import gradle.accessors.projectProperties
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

@Serializable
internal data class CodeOfConductFile(
    override val from: String,
    override val resolution: FileResolution = FileResolution.ABSENT,
    val emailPlaceholder: String,
    val email: String? = null
) : ProjectFile {

    override val into: String
        get() = "./"

    @Transient
    override val replace: MutableMap<String, String> = mutableMapOf()

    context(Project)
    override fun applyTo(name: String): TaskProvider<out DefaultTask> {

        (email ?: projectProperties.developer?.email)?.let { email ->
            replace[emailPlaceholder] = email
        }

        return super.applyTo(name)
    }
}
