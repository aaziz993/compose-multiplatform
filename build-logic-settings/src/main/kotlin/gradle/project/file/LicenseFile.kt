package gradle.project.file

import gradle.accessors.projectProperties
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

@Serializable
internal data class LicenseFile(
    override val from: String,
    override val resolution: FileResolution = FileResolution.ABSENT,
    val yearPlaceholder: String,
    val year: String? = null,
    val ownerPlaceholder: String,
    val owner: String? = null
) : ProjectFile {

    override val into: String
        get() = "./"

    @Transient
    override val replace: MutableMap<String, String> = mutableMapOf()

    context(Project)
    override fun applyTo(name: String): TaskProvider<out DefaultTask> {

        (year ?: projectProperties.year)?.let { year ->
            replace[yearPlaceholder] = year
        }

        (owner ?: projectProperties.developer?.name)?.let { owner ->
            replace[ownerPlaceholder] = owner
        }

        return super.applyTo(name)
    }
}
