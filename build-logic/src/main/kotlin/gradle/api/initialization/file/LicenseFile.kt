package gradle.api.initialization.file

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.Project

@Serializable
public data class LicenseFile(
    val source: String,
    override val resolution: FileResolution = FileResolution.ABSENT,
    val year: String? = null,
    val yearPlaceholder: String = "[yyyy]",
    val owner: String? = null,
    val ownerPlaceholder: String = "[name of copyright owner]"
) : ProjectFile() {

    @Transient
    override val from: MutableList<String> = mutableListOf()

    @Transient
    override val into: String = "LICENSE"

    @Transient
    override val replace: MutableMap<String, String> = mutableMapOf()

    context(project: Project)
    override fun applyTo(receiver: String): List<TaskProvider<out DefaultTask>> {
        from.add(source)
        year?.let { year -> replace[yearPlaceholder] = year }
        owner?.let { owner -> replace[ownerPlaceholder] = owner }

        return super.applyTo(receiver)
    }
}
