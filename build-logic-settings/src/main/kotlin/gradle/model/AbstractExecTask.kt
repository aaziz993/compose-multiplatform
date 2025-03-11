package gradle.model

import gradle.serialization.serializer.AnySerializer
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
    abstract val commandLineArgs: List<@Serializable(with = AnySerializer::class) Any>?

    /**
     * {@inheritDoc}
     */
    abstract val setCommandLineArgs: List<@Serializable(with = AnySerializer::class) Any>?

    /**
     * {@inheritDoc}
     */
    abstract val args: List<@Serializable(with = AnySerializer::class) Any>?

    /**
     * {@inheritDoc}
     */
    abstract val setArgs: List<@Serializable(with = AnySerializer::class) Any>?

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
    abstract val environment: Map<String, @Serializable(with = AnySerializer::class) Any>?

    /**
     * {@inheritDoc}
     */
    abstract val setEnvironment: Map<String, @Serializable(with = AnySerializer::class) Any>?

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
    override val commandLineArgs: List<@Serializable(with = AnySerializer::class) Any>? = null,
    override val setCommandLineArgs: List<@Serializable(with = AnySerializer::class) Any>? = null,
    override val args: List<@Serializable(with = AnySerializer::class) Any>? = null,
    override val setArgs: List<@Serializable(with = AnySerializer::class) Any>? = null,
    override val executable: String? = null,
    override val workingDir: String? = null,
    override val environment: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val setEnvironment: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val ignoreExitValue: Boolean? = null,
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = "",
) : AbstractExecTask<T>()
