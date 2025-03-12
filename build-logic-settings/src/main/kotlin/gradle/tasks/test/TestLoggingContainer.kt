package gradle.tasks.test

import kotlinx.serialization.Serializable
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestLoggingContainer
import org.gradle.api.tasks.testing.logging.TestStackTraceFilter

/**
 * Container for all test logging related options. Different options
 * can be set for each log level. Options that are set directly (without
 * specifying a log level) apply to log level LIFECYCLE. Example:
 *
 * <pre class='autoTested'>
 * apply plugin: 'java'
 *
 * test {
 * testLogging {
 * // set options for log level LIFECYCLE
 * events("failed")
 * exceptionFormat = "short"
 *
 * // set options for log level DEBUG
 * debug {
 * events("started", "skipped", "failed")
 * exceptionFormat = "full"
 * }
 *
 * // remove standard output/error logging from --info builds
 * // by assigning only 'failed' and 'skipped' events
 * info.events = ["failed", "skipped"]
 * }
 * }
</pre> *
 *
 * The defaults that are in place show progressively more information
 * on log levels WARN, LIFECYCLE, INFO, and DEBUG, respectively.
 */
@Serializable
internal data class TestLoggingContainer(
    override val events: Set<TestLogEvent>? = null,
    override val minGranularity: Int? = null,
    override val maxGranularity: Int? = null,
    override val displayGranularity: Int? = null,
    override val showExceptions: Boolean? = null,
    override val showCauses: Boolean? = null,
    override val showStackTraces: Boolean? = null,
    override val exceptionFormat: TestExceptionFormat? = null,
    override val tackTraceFilters: Set<TestStackTraceFilter>? = null,
    override val showStandardStreams: Boolean? = null,
    /**
     * Configures logging options for debug level.
     *
     * @param action logging options for debug level
     */
    val debug: TestLoggingImpl? = null,
    /**
     * Configures logging options for info level.
     *
     * @param action logging options for info level
     */
    val info: TestLoggingImpl? = null,
    /**
     * Configures logging options for lifecycle level.
     *
     * @param action logging options for lifecycle level
     */
    val lifecycle: TestLoggingImpl? = null,
    /**
     * Configures logging options for warn level.
     *
     * @param action logging options for warn level
     */
    val warn: TestLoggingImpl? = null,

    /**
     * Configures logging options for quiet level.
     *
     * @param action logging options for quiet level
     */
    val quiet: TestLoggingImpl? = null,
    /**
     * Configures logging options for error level.
     *
     * @param action logging options for error level
     */
    val error: TestLoggingImpl? = null,
) : TestLogging {

    override fun applyTo(logging: org.gradle.api.tasks.testing.logging.TestLogging) {
        super.applyTo(logging)

        logging as TestLoggingContainer

        debug?.applyTo(logging.debug)
        info?.applyTo(logging.info)
        lifecycle?.applyTo(logging.lifecycle)
        warn?.applyTo(logging.warn)
        quiet?.applyTo(logging.quiet)
        error?.applyTo(logging.error)
    }
}
