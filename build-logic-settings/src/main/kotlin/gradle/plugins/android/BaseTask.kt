package gradle.plugins.android


import gradle.api.tasks.Task
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.kotlin.dsl.withType

/**
 * Root Task class for all of AGP.
 *
 * DO NOT EXTEND THIS METHOD DIRECTLY. Instead, extend:
 * - [NewIncrementalTask] -- variant aware task
 * - [NonIncrementalTask] -- variant aware task
 * - [NonIncrementalGlobalTask] -- non variant aware task
 */
internal abstract class BaseTask : Task {

    abstract val projectPath: String?

        context(Project)
    override fun applyTo(named: T) {
        super.applyTo(named)

        named as com.android.build.gradle.internal.tasks.BaseTask

        named.projectPath tryAssign projectPath
    }

    context(GradleScope)
    override fun _applyTo() = with(project) {
        applyTo(tasks.withType<com.android.build.gradle.internal.tasks.BaseTask>())
    }
}

@Serializable
@SerialName("BaseTask")
internal data class BaseTaskImpl(
    override val projectPath: String? = null,
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = ""
) : BaseTask()
