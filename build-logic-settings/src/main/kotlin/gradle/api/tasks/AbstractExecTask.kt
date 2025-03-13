package gradle.api.tasks

import gradle.collection.SerializableAnyList
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * `AbstractExecTask` is the base class for all exec tasks.
 *
 * @param <T> The concrete type of the class.
</T> */
internal abstract class AbstractExecTask<T : AbstractExecTask<T>> : Task {

    /**
     * {@inheritDoc}
     */
    abstract val commandLineArgs: SerializableAnyList?

    /**
     * {@inheritDoc}
     */
    abstract val setCommandLineArgs: SerializableAnyList?

    /**
     * {@inheritDoc}
     */
    abstract val args: SerializableAnyList?

    /**
     * {@inheritDoc}
     */
    abstract val setArgs: SerializableAnyList?

    /**
     * {@inheritDoc}
     */
    abstract val executable: String?

    /**
     * {@inheritDoc}
     */
    abstract val workingDir: String?

    /**
     * {@inheritDoc}
     */
    abstract val environment: SerializableAnyMap?

    /**
     * {@inheritDoc}
     */
    abstract val setEnvironment: SerializableAnyMap?

    /**
     * {@inheritDoc}
     */
    abstract val ignoreExitValue: Boolean?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.gradle.api.tasks.AbstractExecTask<*>

        commandLineArgs?.let(named::commandLine)
        setCommandLineArgs?.let(named::setCommandLine)
        args?.let(named::args)
        setArgs?.let(named::setArgs)
        executable?.let(named::executable)
        workingDir?.let(named::setWorkingDir)
        environment?.let(named::environment)
        setEnvironment?.let(named::setEnvironment)
        ignoreExitValue?.let(named::setIgnoreExitValue)
    }

    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<org.gradle.api.tasks.AbstractExecTask<*>>())
}

@Serializable
@SerialName("AbstractExecTask")
internal data class AbstractExecTaskImpl<T : AbstractExecTask<T>>(
    override val commandLineArgs: SerializableAnyList? = null,
    override val setCommandLineArgs: SerializableAnyList? = null,
    override val args: SerializableAnyList? = null,
    override val setArgs: SerializableAnyList? = null,
    override val executable: String? = null,
    override val workingDir: String? = null,
    override val environment: SerializableAnyMap? = null,
    override val setEnvironment: SerializableAnyMap? = null,
    override val ignoreExitValue: Boolean? = null,
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
) : AbstractExecTask<T>()
