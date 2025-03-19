package gradle.api.tasks.test

import kotlinx.serialization.Serializable
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestLogging
import org.gradle.api.tasks.testing.logging.TestStackTraceFilter

/**
 * Options that determine which test events get logged, and at which detail.
 */
internal interface TestLogging<in T: TestLogging> {

    /**
     * Sets the events to be logged.
     *
     * @param events the events to be logged
     * @since 4.0
     */
    val events: Set<TestLogEvent>?

    /**
     * Sets the minimum granularity of the events to be logged. Typically, 0 corresponds to events from the Gradle-generated test suite for the whole test run, 1 corresponds to the Gradle-generated test suite
     * for a particular test JVM, 2 corresponds to a test class, and 3 corresponds to a test method. These values may extend higher if user-defined suites or parameterized test methods are executed.  Events
     * from levels lower than the specified granularity will be ignored.
     *
     * The default granularity is -1, which specifies that test events from only the most granular level should be logged.  In other words, if a test method is not parameterized, only events
     * from the test method will be logged and events from the test class and lower will be ignored.  On the other hand, if a test method is parameterized, then events from the iterations of that test
     * method will be logged and events from the test method and lower will be ignored.
     *
     * @param granularity the minimum granularity of the events to be logged
     */
    val minGranularity: Int?

    /**
     * Returns the maximum granularity of the events to be logged. Typically, 0 corresponds to the Gradle-generated test suite for the whole test run, 1 corresponds to the Gradle-generated test suite
     * for a particular test JVM, 2 corresponds to a test class, and 3 corresponds to a test method. These values may extend higher if user-defined suites or parameterized test methods are executed.  Events
     * from levels higher than the specified granularity will be ignored.
     *
     * The default granularity is -1, which specifies that test events from only the most granular level should be logged.  Setting this value to something lower will cause events
     * from a higher level to be ignored.  For example, setting the value to 3 will cause only events from the test method level to be logged and any events from iterations of a parameterized test method
     * will be ignored.
     *
     * Note that since the default value of [.getMinGranularity] is -1 (the highest level of granularity) it only makes sense to configure the maximum granularity while also setting the
     * minimum granularity to a value greater than -1.
     *
     * @param granularity the maximum granularity of the events to be logged
     */
    val maxGranularity: Int?

    /**
     * Sets the display granularity of the events to be logged. For example, if set to 0, a method-level event will be displayed as "Test Run &gt; Test Worker x &gt; org.SomeClass &gt; org.someMethod". If set
     * to 2, the same event will be displayed as "org.someClass &gt; org.someMethod".
     *
     *-1 denotes the highest granularity and corresponds to an atomic test.
     *
     * @param granularity the display granularity of the events to be logged
     */
    val displayGranularity: Int?

    /**
     * Sets whether exceptions that occur during test execution will be logged.  Defaults to true.
     *
     * @param flag whether exceptions that occur during test execution will be logged
     */
    val showExceptions: Boolean?

    /**
     * Sets whether causes of exceptions that occur during test execution will be logged. Only relevant if `showExceptions` is `true`. Defaults to true.
     *
     * @param flag whether causes of exceptions that occur during test execution will be logged
     */
    val showCauses: Boolean?

    /**
     * Sets whether stack traces of exceptions that occur during test execution will be logged.  Defaults to true.
     *
     * @param flag whether stack traces of exceptions that occur during test execution will be logged
     */
    val showStackTraces: Boolean?

    /**
     * Sets the format to be used for logging test exceptions. Only relevant if `showStackTraces` is `true`.  Defaults to [TestExceptionFormat.FULL] for
     * the INFO and DEBUG log levels and [TestExceptionFormat.SHORT] for the LIFECYCLE log level.
     *
     * @param exceptionFormat the format to be used for logging test exceptions
     * @since 4.0
     */
    val exceptionFormat: TestExceptionFormat?

    /**
     * Sets the set of filters to be used for sanitizing test stack traces.
     *
     * @param stackTraces the set of filters to be used for sanitizing test stack traces
     * @since 4.0
     */
    val tackTraceFilters: Set<TestStackTraceFilter>?

    /**
     * Sets whether output on standard out and standard error will be logged. Equivalent to setting log events `TestLogEvent.STANDARD_OUT` and `TestLogEvent.STANDARD_ERROR`.
     */
    val showStandardStreams: Boolean?

    fun applyTo(recipient: T) {
        events?.let(recipient::setEvents)
        minGranularity?.let(recipient::setMinGranularity)
        maxGranularity?.let(recipient::setMaxGranularity)
        displayGranularity?.let(recipient::setDisplayGranularity)
        showExceptions?.let(recipient::setShowExceptions)
        showCauses?.let(recipient::setShowCauses)
        showStackTraces?.let(recipient::setShowStackTraces)
        exceptionFormat?.let(recipient::setExceptionFormat)
        tackTraceFilters?.let(recipient::setStackTraceFilters)
        showStandardStreams?.let(recipient::setShowStandardStreams)
    }
}

@Serializable
internal data class TestLoggingImpl(
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
) : gradle.api.tasks.test.TestLogging<TestLogging>
