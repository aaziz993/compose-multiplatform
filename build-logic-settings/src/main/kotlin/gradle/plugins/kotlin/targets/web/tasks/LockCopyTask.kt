package gradle.plugins.kotlin.targets.web.tasks

import gradle.api.file.tryAssign
import gradle.api.file.tryFrom
import gradle.api.file.trySetFrom
import gradle.api.provider.tryAssign
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import klib.data.type.serialization.serializer.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class LockCopyTask<T : org.jetbrains.kotlin.gradle.targets.js.npm.LockCopyTask> : DefaultTask<T>() {

    abstract val inputFile: String?

    abstract val additionalInputFiles: Set<String>?

    abstract val setAdditionalInputFiles: Set<String>?

    abstract val outputDirectory: String?

    abstract val fileName: String?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.inputFile tryAssign inputFile?.let(project::file)
        receiver.additionalInputFiles tryFrom additionalInputFiles
        receiver.additionalInputFiles trySetFrom setAdditionalInputFiles
        receiver.outputDirectory tryAssign outputDirectory?.let(project.layout.projectDirectory::dir)
        receiver.fileName tryAssign fileName
    }
}

@Serializable
@SerialName("LockCopyTask")
internal data class LockCopyTaskImple(
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
) : LockCopyTask<org.jetbrains.kotlin.gradle.targets.js.npm.LockCopyTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.targets.js.npm.LockCopyTask>())
}
