package gradle.plugins.web.yarn.tasks

import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import gradle.plugins.web.tasks.LockCopyTask
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockCopyTask

@Serializable
internal data class YarnLockCopyTask(
    override val inputFile: String? = null,
    override val additionalInputFiles: Set<String>? = null,
    override val setAdditionalInputFiles: Set<String>? = null,
    override val outputDirectory: String? = null,
    override val fileName: String? = null,
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
) : LockCopyTask<YarnLockCopyTask>() {

    context(project: Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<YarnLockCopyTask>())
}
