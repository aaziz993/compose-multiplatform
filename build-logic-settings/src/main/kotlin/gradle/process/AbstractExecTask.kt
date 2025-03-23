package gradle.process

import gradle.api.tasks.ConventionTask
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyList
import gradle.collection.SerializableAnyMap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * `AbstractExecTask` is the base class for all exec tasks.
 *
 * @param <T> The concrete type of the class.
</T> */
internal abstract class AbstractExecTask<T : org.gradle.api.tasks.AbstractExecTask<*>>
    : ConventionTask<T>(), ExecSpec<T> {

    context(Project)
    override fun applyTo(receiver: T) {
        super<ConventionTask>.applyTo(receiver)
        super<ExecSpec>.applyTo(receiver)
    }
}

@Serializable
@SerialName("AbstractExecTask")
internal data class AbstractExecTaskImpl(
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
) : AbstractExecTask<org.gradle.api.tasks.AbstractExecTask<*>>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.gradle.api.tasks.AbstractExecTask<*>>())
}
