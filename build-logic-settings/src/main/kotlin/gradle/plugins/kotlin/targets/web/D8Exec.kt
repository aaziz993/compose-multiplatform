package gradle.plugins.kotlin.targets.web

import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.api.trySet
import gradle.collection.SerializableAnyList
import gradle.collection.SerializableAnyMap
import gradle.process.AbstractExecTask
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.d8.D8Exec

@Serializable
internal data class D8Exec(
    override val commandLine: SerializableAnyList? = null,
    override val setCommandLine: SerializableAnyList? = null,
    override val args: SerializableAnyList? = null,
    override val setArgs: SerializableAnyList? = null,
    override val executable: String? = null,
    override val workingDir: String? = null,
    override val environment: SerializableAnyMap? = null,
    override val setEnvironment: SerializableAnyMap? = null,
    override val ignoreExitValue: Boolean? = null,
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
    val d8Args: List<String>? = null,
    val inputFileProperty: String? = null,
) : AbstractExecTask<D8Exec>() {

    context(Project)
    override fun applyTo(receiver: D8Exec) {
        super.applyTo(receiver)

        receiver::d8Args trySet d8Args?.toMutableList()

        receiver.inputFileProperty tryAssign inputFileProperty?.let(project::file)
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<D8Exec>())
}
