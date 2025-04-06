package gradle.api.tasks.test

import gradle.api.provider.tryAssign
import gradle.api.tasks.ConventionTask
import gradle.api.tasks.VerificationTask
import gradle.api.tasks.applyTo
import klib.data.type.serialization.json.serializer.SerializableAnyMap
import klib.data.type.reflection.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class AbstractTestTask<T : org.gradle.api.tasks.testing.AbstractTestTask>
    : ConventionTask<T>(), VerificationTask<org.gradle.api.tasks.VerificationTask> {

    /**
     * Returns the root directory property for the test results in internal binary format.
     *
     * @since 4.4
     */
    abstract val binaryResultsDirectory: String?

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

    abstract val filter: TestFilter?

    context(Project)
    override fun applyTo(receiver: T) {
        super<ConventionTask>.applyTo(receiver)
        super<VerificationTask>.applyTo(receiver)

        receiver.binaryResultsDirectory tryAssign binaryResultsDirectory?.let(project.layout.projectDirectory::dir)
        receiver::setIgnoreFailures trySet ignoreFailures
        testLogging?.applyTo(receiver.testLogging)
        receiver::setTestNameIncludePatterns trySet testNameIncludePatterns
        filter?.applyTo(receiver.filter)
    }
}

@Serializable
@SerialName("AbstractTestTask")
internal data class AbstractTestTaskImpl(
    override val binaryResultsDirectory: String? = null,
    override val ignoreFailures: Boolean? = null,
    override val testLogging: TestLoggingContainer? = null,
    override val testNameIncludePatterns: List<String>? = null,
    override val failFast: Boolean? = null,
    override val filter: TestFilter? = null,
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
) : AbstractTestTask<org.gradle.api.tasks.testing.AbstractTestTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.gradle.api.tasks.testing.AbstractTestTask>())
}
