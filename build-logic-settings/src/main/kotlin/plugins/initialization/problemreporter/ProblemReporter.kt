package plugins.initialization.problemreporter

internal interface ProblemReporter {

    /**
     * Check if we reported any fatal errors.
     */
    val hasFatal: Boolean

    fun reportMessage(message: BuildProblem)
}

internal interface ProblemReporterContext {

    val problemReporter: ProblemReporter
}

// TODO: Can be refactored to the reporter chain to avoid inheritance.
// Note: This class is not thread-safe.
// Problems collecting might misbehave when used from multiple threads (e.g. in Gradle).
internal abstract class CollectingProblemReporter : ProblemReporter {

    override val hasFatal get() = problems.any { it.level == Level.Fatal }

    protected val problems: MutableList<BuildProblem> = mutableListOf()

    protected abstract fun doReportMessage(message: BuildProblem)

    override fun reportMessage(message: BuildProblem) {
        problems.add(message)
        doReportMessage(message)
    }
}

internal class NoOpCollectingProblemReporter : CollectingProblemReporter() {

    fun getProblems(): Collection<BuildProblem> = problems

    override fun doReportMessage(message: BuildProblem) {
    }
}

@OptIn(NonIdealDiagnostic::class)
internal fun renderMessage(problem: BuildProblem): String = buildString {
    fun appendSource(source: BuildProblemSource) {
        when (source) {
            is FileBuildProblemSource -> {
                append(source.file.normalize())
                if (source is FileWithRangesBuildProblemSource) {
                    val start = source.range.start
                    append(":${start.line}:${start.column}")
                }
                append(": ")
                append(problem.message)
            }

            is MultipleLocationsBuildProblemSource -> {
                source.sources.dropLast(1).forEach {
                    appendSource(it)
                    appendLine()
                }
                source.sources.lastOrNull()?.let(::appendSource)
            }

            GlobalBuildProblemSource -> append(problem.message)
        }
    }

    appendSource(problem.source)
}
