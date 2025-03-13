package gradle.api.tasks.copy

import gradle.api.tasks.FilesMatching
import gradle.api.tasks.PatternFilterable
import org.gradle.api.Project
import org.gradle.api.file.CopySpec
import org.gradle.api.file.DuplicatesStrategy

/**
 * A set of specifications for copying files.  This includes:
 *
 *
 *
 *  * source directories (multiples allowed)
 *
 *  * destination directory
 *
 *  * ANT like include patterns
 *
 *  * ANT like exclude patterns
 *
 *  * File relocating rules
 *
 *  * renaming rules
 *
 *  * content filters
 *
 *
 *
 * CopySpecs may be nested by passing a closure to one of the from methods.  The closure creates a child CopySpec and
 * delegates methods in the closure to the child. Child CopySpecs inherit any values specified in the parent. This
 * allows constructs like:
 * <pre class='autoTested'>
 * def myCopySpec = project.copySpec {
 * into('webroot')
 * exclude('**&#47;.data/ **')
 * from('src/main/webapp') {
 * include '**&#47;*.jsp'
 * }
 * from('src/main/js') {
 * include '**&#47;*.js'
 * }
 * }
</pre> *
 *
 * In this example, the `into` and `exclude` specifications at the root level are inherited by the
 * two child CopySpecs.
 *
 * Copy specs can be reused in other copy specs via [.with] method. This enables reuse of the copy spec instances.
 *
 * <pre class='autoTested'>
 * def contentSpec = copySpec {
 * from("content") {
 * include "**&#47;*.txt"
 * }
 * }
 *
 * task copy(type: Copy) {
 * into "$buildDir/copy"
 * with contentSpec
 * }
</pre> *
 *
 * @see org.gradle.api.tasks.Copy Copy Task
 *
 * @see Project.copy
 */
internal interface CopySpec : CopySourceSpec, CopyProcessingSpec, PatternFilterable {

    /**
     * Specifies whether case-sensitive pattern matching should be used.
     *
     * @return true for case-sensitive matching.
     */
    val isCaseSensitive: Boolean?

    /**
     * Controls if empty target directories should be included in the copy.
     *
     * @param includeEmptyDirs `true` if empty target directories should be included in the copy, `false` otherwise
     */
    val includeEmptyDirs: Boolean?

    /**
     * The strategy to use when trying to copy more than one file to the same destination. Set to [DuplicatesStrategy.INHERIT], the default strategy, to use
     * the strategy inherited from the parent copy spec, if any, or [DuplicatesStrategy.INCLUDE] if this copy spec has no parent.
     */
    val duplicatesStrategy: DuplicatesStrategy?

    /**
     * Configure the [org.gradle.api.file.FileCopyDetails] for each file whose path matches any of the specified Ant-style patterns.
     * This is equivalent to using eachFile() and selectively applying a configuration based on the file's path.
     *
     * @param patterns Ant-style patterns used to match against files' relative paths
     * @param action action called for the FileCopyDetails of each file matching pattern
     * @return this
     */
    val filesMatching: FilesMatching?

    /**
     * Configure the [org.gradle.api.file.FileCopyDetails] for each file whose path does not match any of the specified
     * Ant-style patterns. This is equivalent to using eachFile() and selectively applying a configuration based on the
     * file's path.
     *
     * @param patterns Ant-style patterns used to match against files' relative paths
     * @param action action called for the FileCopyDetails of each file that does not match any pattern
     * @return this
     */
    val filesNotMatching: FilesMatching?

    /**
     * Specifies the charset used to read and write files when filtering.
     *
     * @param charset the name of the charset to use when filtering files
     * @since 2.14
     */
    val filteringCharset: String?

    context(Project)
    fun applyTo(spec: CopySpec) {
        super<CopySourceSpec>.applyTo(spec)

        super<CopyProcessingSpec>.applyTo(spec)

        super<PatternFilterable>.applyTo(spec)

        isCaseSensitive?.let(spec::setCaseSensitive)

        includeEmptyDirs?.let(spec::setIncludeEmptyDirs)

        duplicatesStrategy?.let(spec::setDuplicatesStrategy)

        filesMatching?.let { filesMatching ->
            spec.filesMatching(filesMatching.patterns, filesMatching.fileCopyDetails::applyTo)
        }

        filesNotMatching?.let { filesNotMatching ->
            spec.filesNotMatching(filesNotMatching.patterns, filesNotMatching.fileCopyDetails::applyTo)
        }

        filteringCharset?.let(spec::setFilteringCharset)
    }
}

