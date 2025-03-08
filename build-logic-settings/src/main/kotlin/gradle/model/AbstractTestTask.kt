package gradle.model

import gradle.tryAssign
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.tasks.testing.AbstractTestTask

internal interface AbstractTestTask : Task {

    /**
     * Returns the root directory property for the test results in internal binary format.
     *
     * @since 4.4
     */
    val binaryResultsDirectory: String?

    /**
     * {@inheritDoc}
     */
    val ignoreFailures: Boolean?

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
    val testLogging: TestLoggingContainer?

    /**
     * Sets the test name patterns to be included in execution.
     * Classes or method names are supported, wildcard '*' is supported.
     * For more information see the user guide chapter on testing.
     *
     * For more information on supported patterns see [TestFilter]
     */
    val testNameIncludePatterns: List<String>?

    val failFast: Boolean?

    val filter: DefaultTestFilter?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as AbstractTestTask

        named.binaryResultsDirectory tryAssign binaryResultsDirectory?.let(layout.projectDirectory::dir)
        ignoreFailures?.let(named::setIgnoreFailures)
        testLogging?.applyTo(named.testLogging)
        testNameIncludePatterns?.let(named::setTestNameIncludePatterns)
        filter?.applyTo(named.filter)
    }
}
