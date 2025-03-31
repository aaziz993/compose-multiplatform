package gradle.api.initialization.problemreporter

import org.slf4j.LoggerFactory

public class SLF4JProblemReporter(loggerClass: Class<*> = ProblemReporter::class.java) : CollectingProblemReporter() {
    public companion object {

        private const val ERROR_PREFIX = "  - "
        private const val ERROR_INDENT = "    "
    }

    private val logger = LoggerFactory.getLogger(loggerClass)

    override fun doReportMessage(message: BuildProblem) {
        when (message.level) {
            Level.Warning -> logger.warn(renderMessage(message))
            Level.Error -> logger.error(renderMessage(message))
            Level.Fatal -> logger.error(renderMessage(message))
            Level.Redundancy -> logger.info(renderMessage(message))
        }
    }

    public fun getErrors(): List<String> = problems.map(::renderMessage)

    public fun getGradleError(): String =
        """
        |Amper model initialization failed.
        |Errors:
        |$ERROR_PREFIX${getErrors().joinToString("\n|$ERROR_PREFIX") { it.replace("\n", "\n$ERROR_INDENT") }}
        |See logs for details.""".trimMargin()
}
