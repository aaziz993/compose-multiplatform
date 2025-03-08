package gradle.model

import kotlinx.serialization.Serializable
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestStackTraceFilter

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
) : TestLogging
