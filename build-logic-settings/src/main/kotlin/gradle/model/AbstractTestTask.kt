package gradle.model

import gradle.serialization.serializer.AnySerializer
import gradle.tryAssign
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal abstract class AbstractTestTask : Task {

    /**
     * Returns the root directory property for the test results in internal binary format.
     *
     * @since 4.4
     */
    abstract val binaryResultsDirectory: String?

    /**
     * {@inheritDoc}
     */
    abstract val ignoreFailures: Boolean?

    /**
     * Allows configuring the logging of the test execution, for example log eagerly the standard output, etc.
     *
     * <pre class='autoTested'>
     * apply plugin: 'java'
     *
     * // makes the standard streams (err and out) visible at console when running tests
     * test.testLogging {
     * showStandardStreams = true
     * }
    </pre> *
     *
     * @param action configure action
     * @since 3.5
     */
    abstract val testLogging: TestLoggingContainer?

    /**
     * Sets the test name patterns to be included in execution.
     * Classes or method names are supported, wildcard '*' is supported.
     * For more information see the user guide chapter on testing.
     *
     * For more information on supported patterns see [TestFilter]
     */
    abstract val testNameIncludePatterns: List<String>?

    abstract val failFast: Boolean?

    abstract val filter: DefaultTestFilter?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.gradle.api.tasks.testing.AbstractTestTask

        named.binaryResultsDirectory tryAssign binaryResultsDirectory?.let(layout.projectDirectory::dir)
        ignoreFailures?.let(named::setIgnoreFailures)
        testLogging?.applyTo(named.testLogging)
        testNameIncludePatterns?.let(named::setTestNameIncludePatterns)
        filter?.applyTo(named.filter)
    }

    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<org.gradle.api.tasks.testing.AbstractTestTask>())
}

@Serializable
@SerialName("AbstractTestTask")
internal data class AbstractTestTaskImpl(
    override val binaryResultsDirectory: String? = null,
    override val ignoreFailures: Boolean? = null,
    override val testLogging: TestLoggingContainer? = null,
    override val testNameIncludePatterns: List<String>? = null,
    override val failFast: Boolean? = null,
    override val filter: DefaultTestFilter? = null,
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
) : AbstractTestTask()
