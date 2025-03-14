package gradle.plugins.android

import gradle.collection.SerializableAnyMap
import gradle.plugins.cmp.desktop.AbstractComposeDesktopTask
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
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
internal abstract class AndroidVariantTask : BaseTask(), VariantTask {

    context(Project)
    override fun applyTo(named: Named) {
        super<BaseTask>.applyTo(named)

        named as com.android.build.gradle.internal.tasks.AndroidVariantTask

        super<VariantTask>.applyTo(named)
    }

    context(Project)
    override fun applyTo() =
        super<BaseTask>.applyTo(tasks.withType<com.android.build.gradle.internal.tasks.AndroidVariantTask>())
}

@Serializable
@SerialName("AndroidVariantTask")
internal data class AndroidVariantTaskImpl(
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
) : AndroidVariantTask()
