package gradle.plugins.android.tasks

import gradle.api.provider.tryAssign
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import klib.data.type.serialization.json.serializer.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Root Task class for all of AGP.
 *
 * DO NOT EXTEND THIS METHOD DIRECTLY. Instead, extend:
 * - [NewIncrementalTask] -- variant aware task
 * - [NonIncrementalTask] -- variant aware task
 * - [NonIncrementalGlobalTask] -- non variant aware task
 */
internal abstract class BaseTask<T : com.android.build.gradle.internal.tasks.BaseTask> : DefaultTask<T>() {

    abstract val projectPath: String?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.projectPath tryAssign projectPath
    }
}

@Serializable
@SerialName("BaseTask")
internal data class BaseTaskImpl(
    override val projectPath: String? = null,
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
) : BaseTask<com.android.build.gradle.internal.tasks.BaseTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<com.android.build.gradle.internal.tasks.BaseTask>())
}
