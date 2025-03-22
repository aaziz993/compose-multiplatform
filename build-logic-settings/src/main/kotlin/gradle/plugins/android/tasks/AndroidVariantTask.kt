package gradle.plugins.android.tasks

import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Base Android task with a variant name and support for analytics
 *
 * DO NOT EXTEND THIS METHOD DIRECTLY. Instead extend:
 * - [NewIncrementalTask]
 * - [NonIncrementalTask]
 *
 */
internal abstract class AndroidVariantTask<T : com.android.build.gradle.internal.tasks.AndroidVariantTask> : BaseTask<T>(), VariantTask {

    context(Project)
    override fun applyTo(recipient: T) {
        super<BaseTask>.applyTo(recipient)
        super<VariantTask>.applyTo(recipient)
    }
}

@Serializable
@SerialName("AndroidVariantTask")
internal data class AndroidVariantTaskImpl(
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
    override val name: String? = null,,
    override val variantName: String? = null,
) : AndroidVariantTask<com.android.build.gradle.internal.tasks.AndroidVariantTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<com.android.build.gradle.internal.tasks.AndroidVariantTask>())
}
