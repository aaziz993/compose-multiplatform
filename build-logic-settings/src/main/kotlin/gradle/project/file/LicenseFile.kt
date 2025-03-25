package gradle.project.file

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.api.publish.maven.MavenPomDeveloper
import gradle.api.publish.maven.MavenPomLicense
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

@Serializable
internal data class LicenseFile(
    val source: String,
    override val into: String,
    override val resolution: FileResolution = FileResolution.ABSENT,
    val yearPlaceholder: String,
    val year: String? = null,
    val ownerPlaceholder: String,
    val owner: String? = null
) : ProjectFile {

    @Transient
    override val from: List<String> = listOf(source)

    @Transient
    override val replace: MutableMap<String, String> = mutableMapOf()

    context(project: Project)
    override fun applyTo(receiver: String): List<TaskProvider<out DefaultTask>> {
        (year ?: project.settings.projectProperties.year)?.let { year ->
            replace[yearPlaceholder] = year
        }

        (owner ?: project.settings.projectProperties.developers
            ?.mapNotNull(MavenPomDeveloper::name)
            ?.joinToString(", "))?.let { owner ->
            replace[ownerPlaceholder] = owner
        }

        return super.applyTo(receiver)
    }
}
