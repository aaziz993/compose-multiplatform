package plugin.project.gradle.model

import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project

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
}
