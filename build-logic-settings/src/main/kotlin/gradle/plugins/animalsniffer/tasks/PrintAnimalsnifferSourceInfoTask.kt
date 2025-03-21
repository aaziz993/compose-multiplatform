package gradle.plugins.animalsniffer.tasks

import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import ru.vyarus.gradle.plugin.animalsniffer.debug.PrintAnimalsnifferSourceInfoTask

@Serializable
internal data class PrintAnimalsnifferSourceInfoTask(
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
) : DefaultTask<PrintAnimalsnifferSourceInfoTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<PrintAnimalsnifferSourceInfoTask>())
}
