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
    val inputFiles: Set<String>? = null,
    val destinationDirectory: String? = null,
    val classPath: Set<String>? = null,
    val jvmVariant: String? = null,
    val verbose: Boolean? = null,
) : ConventionTask<AtomicFUTransformTask>() {

    context(Project)
    override fun applyTo(receiver: AtomicFUTransformTask) {
        super.applyTo(receiver)

        receiver::inputFiles trySet inputFiles?.toTypedArray()?.let(project::files)
        receiver.destinationDirectory tryAssign destinationDirectory?.let(project.layout.projectDirectory::dir)
        receiver::classPath trySet classPath?.toTypedArray()?.let(project::files)
        receiver::jvmVariant trySet jvmVariant
        receiver::verbose trySet verbose
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<AtomicFUTransformTask>())
}
