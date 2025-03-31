package gradle.api.initialization.problemreporter

import java.nio.file.Path

public typealias BuildProblemId = String

public enum class Level {
    /**
     * Cannot process build or import further.
     */
    Fatal,

    /**
     * Can partially finish import or build. Overall build cannot be finished.
     */
    Error,

    /**
     * Can finish import and build.
     */
    Warning,

    /**
     * Can finish import and build.
     * Marks as a redundant declaration ("dead code" in the IDE, INFO level message in console)
     */
    Redundancy,
}

/**
 * Designates the place where the cause of the problem is located.
 */
public sealed interface BuildProblemSource

/**
 * Use only when there is no way to pinpoint the cause of the problem inside the Amper files.
 */
@NonIdealDiagnostic
public object GlobalBuildProblemSource : BuildProblemSource

/**
 * Can be used to express the problem with multiple locations (e.g., conflicting declarations).
 */
public class MultipleLocationsBuildProblemSource(public val sources: List<BuildProblemSource>) : BuildProblemSource {

    public constructor(vararg sources: BuildProblemSource) : this(sources.toList())

    init {
        require(sources.none { it is MultipleLocationsBuildProblemSource }) { "Only non-nested sources are allowed in a MultipleLocationsBuildProblemSource" }
    }
}

public interface FileBuildProblemSource : BuildProblemSource {

    /**
     * Path to the file containing a problem.
     */
    public val file: Path
}

public interface FileWithRangesBuildProblemSource : FileBuildProblemSource {

    /**
     * Range of problematic code expressed in terms of lines and columns.
     * Can be used by clients to render the links to the exact location in the file or display an erroneous part of the
     * code.
     */
    public val range: LineAndColumnRange

    /**
     * Range of problematic code expressed in terms of character offsets inside the file.
     * Depending on the client, it might choose [range] or [offsetRange] for displaying an error.
     * The choice depends on what primitives does the client operate with.
     */
    public val offsetRange: IntRange
}

public interface BuildProblem {

    public val buildProblemId: BuildProblemId
    public val source: BuildProblemSource
    public val message: String
    public val level: Level
}

/**
 * Prefer writing strongly typed build problems
 * (see inheritors of [PsiBuildProblem][org.jetbrains.amper.frontend.messages.PsiBuildProblem] for a reference).
 * They can incorporate additional properties for the IDE to simplify quick-fixes implementation.
 */
public data class BuildProblemImpl(
    override val buildProblemId: BuildProblemId,
    override val source: BuildProblemSource,
    override val message: String,
    override val level: Level,
) : BuildProblem

public data class LineAndColumn(val line: Int, val column: Int, val lineContent: String?) {
    public companion object {

        public val NONE: LineAndColumn = LineAndColumn(-1, -1, null)
    }
}

/**
 * This range should be interpreted as all the symbols between [start] and [end] inclusive.
 * All the intermediate lines between [start] and [end] are included entirely.
 */
public data class LineAndColumnRange(val start: LineAndColumn, val end: LineAndColumn)
