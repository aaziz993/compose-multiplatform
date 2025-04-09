package gradle.plugins.kotlin.targets.web.tasks

import gradle.api.provider.tryAssign
import gradle.api.tasks.applyTo
import klib.data.type.serialization.serializer.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.npm.LockFileMismatchReport

internal abstract class LockStoreTask<T : org.jetbrains.kotlin.gradle.targets.js.npm.LockStoreTask> : LockCopyTask<T>() {

    abstract val lockFileMismatchReport: LockFileMismatchReport?

    abstract val reportNewLockFile: Boolean?

    abstract val lockFileAutoReplace: Boolean?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.lockFileMismatchReport tryAssign lockFileMismatchReport
        receiver.reportNewLockFile tryAssign reportNewLockFile
        receiver.lockFileAutoReplace tryAssign lockFileAutoReplace
    }
}

@Serializable
@SerialName("LockStoreTask")
internal data class LockStoreTaskImpl(
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
    override val lockFileMismatchReport: LockFileMismatchReport? = null,
    override val reportNewLockFile: Boolean? = null,
    override val lockFileAutoReplace: Boolean? = null,
    override val inputFile: String? = null,
    override val additionalInputFiles: Set<String>? = null,
    override val setAdditionalInputFiles: Set<String>? = null,
    override val outputDirectory: String? = null,
    override val fileName: String? = null,
    override val name: String? = null,
) : LockStoreTask<org.jetbrains.kotlin.gradle.targets.js.npm.LockStoreTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.targets.js.npm.LockStoreTask>())
}
