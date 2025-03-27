package gradle.process

import gradle.collection.SerializableAnyMap
import org.gradle.api.Project
import org.gradle.process.ProcessForkOptions

/**
 *
 * Specifies the options to use to fork a process.
 */
internal interface ProcessForkOptions<T : ProcessForkOptions> {

    /**
     * Sets the name of the executable to use.
     *
     * @param executable The executable. Must not be null.
     * @since 4.0
     */
    val executable: String?

    /**
     * Sets the working directory for the process.
     *
     * @param dir The working directory. Must not be null.
     * @since 4.0
     */
    val workingDir: String?

    /**
     * Sets the environment variable to use for the process.
     *
     * @param environmentVariables The environment variables. Must not be null.
     */
    val environment: SerializableAnyMap?
    val setEnvironment: SerializableAnyMap?

    context(Project)
    fun applyTo(receiver: T) {
        receiver::setExecutable trySet executable
        receiver::setWorkingDir trySet workingDir
        receiver::environment trySet environment
        receiver::setEnvironment trySet setEnvironment
    }
}
