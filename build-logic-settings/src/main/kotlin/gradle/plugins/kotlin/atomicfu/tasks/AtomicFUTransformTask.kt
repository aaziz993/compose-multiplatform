package gradle.plugins.kotlin.atomicfu.tasks

import gradle.api.tasks.ConventionTask
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import kotlinx.atomicfu.plugin.gradle.AtomicFUTransformTask
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class AtomicFUTransformTask(
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
    var inputFiles: Set<String>? = null,
    val destinationDirectory: String? = null,
    var classPath: Set<String>? = null,
    var jvmVariant: String? = null,
    var verbose: Boolean? = null,
) : ConventionTask<AtomicFUTransformTask>() {

    context(Project)
    override fun applyTo(recipient: AtomicFUTransformTask) {
        super.applyTo(recipient)

        recipient::inputFiles trySet inputFiles?.toTypedArray()?.let(::files)
        recipient.destinationDirectory tryAssign destinationDirectory?.let(layout.projectDirectory::dir)
        recipient::classPath trySet classPath?.toTypedArray()?.let(::files)
        recipient::jvmVariant trySet jvmVariant
        recipient::verbose trySet verbose
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<AtomicFUTransformTask>())
}
