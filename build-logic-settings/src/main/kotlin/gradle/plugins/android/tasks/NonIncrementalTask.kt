package gradle.plugins.android.tasks

import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Base variant-aware non-incremental task
 */
internal abstract class NonIncrementalTask<T : com.android.build.gradle.internal.tasks.NonIncrementalTask> : AndroidVariantTask<T>() {
}

@Serializable
@SerialName("NonIncrementalTask")
internal data class NonIncrementalTaskImpl(
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
    override val variantName: String? = null,
) : NonIncrementalTask<com.android.build.gradle.internal.tasks.NonIncrementalTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<com.android.build.gradle.internal.tasks.NonIncrementalTask>())
}
