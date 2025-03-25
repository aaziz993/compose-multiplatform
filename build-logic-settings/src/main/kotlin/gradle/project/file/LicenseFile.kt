package gradle.project.file

import gradle.accessors.projectProperties
import gradle.accessors.settings
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

@Serializable
internal data class LicenseFile(
    val source: String? = null,
    override val resolution: FileResolution = FileResolution.ABSENT,
    val yearPlaceholder: String,
    val year: String? = null,
    val ownerPlaceholder: String,
    val owner: String? = null
) : ProjectFile {

    @Transient
    override val from: MutableList<String> = mutableListOf()

    @Transient
    override val into: String = "LICENSE"

    @Transient
    override val replace: MutableMap<String, String> = mutableMapOf()

    context(Project)
    override fun applyTo(receiver: String): List<TaskProvider<out DefaultTask>> {
        from.add(source ?: project.settings.projectProperties.license?.url ?: return emptyList())

        (year ?: project.settings.projectProperties.year)?.let { year ->
            replace[yearPlaceholder] = year
        }

        (owner ?: project.settings.projectProperties.developer?.name)?.let { owner ->
            replace[ownerPlaceholder] = owner
        }

        return super.applyTo(receiver)
    }
}
