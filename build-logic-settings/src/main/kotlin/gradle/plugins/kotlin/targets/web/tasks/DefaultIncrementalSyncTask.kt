package gradle.plugins.kotlin.targets.web.tasks

import org.gradle.kotlin.dsl.withType
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.plugins.kotlin.tasks.IncrementalSyncTask
import klib.data.type.serialization.json.serializer.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy

internal abstract class DefaultIncrementalSyncTask<T : org.jetbrains.kotlin.gradle.targets.js.ir.DefaultIncrementalSyncTask>
    : DefaultTask<T>(), IncrementalSyncTask<T> {

    context(Project)
    override fun applyTo(receiver: T) {
        super<DefaultTask>.applyTo(receiver)
        super<IncrementalSyncTask>.applyTo(receiver)
    }
}

@Serializable
@SerialName("DefaultIncrementalSyncTask")
internal data class DefaultIncrementalSyncTaskImpl(
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
    override val name: String? = null,
    override val from: Set<String>? = null,
    override val setFrom: Set<String>? = null,
    override val duplicatesStrategy: DuplicatesStrategy? = null,
    override val destinationDirectory: String? = null,
) : DefaultIncrementalSyncTask<org.jetbrains.kotlin.gradle.targets.js.ir.DefaultIncrementalSyncTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.targets.js.ir.DefaultIncrementalSyncTask>())
}
