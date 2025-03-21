package gradle.plugins.animalsniffer.tasks

import org.gradle.kotlin.dsl.withType
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import ru.vyarus.gradle.plugin.animalsniffer.debug.PrintAnimalsnifferTasksTask

@Serializable
internal data class PrintAnimalsnifferTasksTask(
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String = ""
) : DefaultTask<PrintAnimalsnifferTasksTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<PrintAnimalsnifferTasksTask>())
}
