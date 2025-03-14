package gradle.plugins.android

import gradle.collection.SerializableAnyMap
import gradle.plugins.android.BaseTask
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Base variant-aware non-incremental task
 */
internal abstract class NonIncrementalTask : AndroidVariantTask() {


    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<com.android.build.gradle.internal.tasks.NonIncrementalTask>())
}

@Serializable
@SerialName("NonIncrementalTask")
internal data class NonIncrementalTaskImpl(
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
    override val name: String = "",
    override val variantName: String? = null,
) : NonIncrementalTask()
