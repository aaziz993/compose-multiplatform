package gradle.model

import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable
import org.gradle.process.ProcessForkOptions

/**
 *
 * Specifies the options to use to fork a process.
 */
internal interface ProcessForkOptions {

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
    val environment: Map<String, @Serializable(with = AnySerializer::class) Any>?

    fun applyTo(options: ProcessForkOptions) {
        executable?.let(options::executable)
        workingDir?.let(options::workingDir)
        environment?.let(options::environment)
    }
}
