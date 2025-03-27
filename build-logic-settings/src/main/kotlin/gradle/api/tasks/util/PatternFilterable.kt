package gradle.api.tasks.util

import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 *
 * A `PatternFilterable` represents some file container which Ant-style include and exclude patterns or specs
 * can be applied to.
 *
 *
 * Patterns may include:
 *
 *
 *
 *  * '*' to match any number of characters
 *
 *  * '?' to match any single character
 *
 *  * '**' to match any number of directories or files
 *
 *
 *
 *
 * Either '/' or '\' may be used in a pattern to separate directories. Patterns ending with '/' or '\' will have '**'
 * automatically appended.
 *
 *
 * Examples:
 * <pre>
 * all files ending with 'jsp' (including subdirectories)
 * **&#47;*.jsp
 *
 * all files beginning with 'template_' in the level1/level2 directory
 * level1/level2/template_*
 *
 * all files (including subdirectories) beneath src/main/webapp
 * src/main/webapp/
 *
 * all files beneath any .svn directory (including subdirectories) under src/main/java
 * src/main/java/ **&#47;.svn/ **
</pre> *
 *
 *
 * You may also use a closure or [org.gradle.api.specs.Spec] to specify which files to include or exclude. The closure or [org.gradle.api.specs.Spec]
 * is passed a [org.gradle.api.file.FileTreeElement], and must return a boolean value.
 *
 *
 * If no include patterns or specs are specified, then all files in this container will be included. If any include
 * patterns or specs are specified, then a file is included if it matches any of the patterns or specs.
 *
 *
 * If no exclude patterns or spec are specified, then no files will be excluded. If any exclude patterns or specs are
 * specified, then a file is include only if it matches none of the patterns or specs.
 */
internal interface PatternFilterable<T : org.gradle.api.tasks.util.PatternFilterable> {

    /**
     * Add the allowable include patterns.  Note that unlike [.include] this replaces any previously
     * defined includes.
     *
     * @param includes an Iterable providing new include patterns
     * @return this
     * @see PatternFilterable Pattern Format
     */
    val includes: Set<String>?

    /**
     * Set the allowable include patterns.  Note that unlike [.include] this replaces any previously
     * defined includes.
     *
     * @param includes an Iterable providing new include patterns
     * @return this
     * @see PatternFilterable Pattern Format
     */
    val setIncludes: Set<String>?

    /**
     * Add the allowable exclude patterns.  Note that unlike [.exclude] this replaces any previously
     * defined excludes.
     *
     * @param excludes an Iterable providing new exclude patterns
     * @return this
     * @see PatternFilterable Pattern Format
     */
    val excludes: Set<String>?

    /**
     * Set the allowable exclude patterns.  Note that unlike [.exclude] this replaces any previously
     * defined excludes.
     *
     * @param excludes an Iterable providing new exclude patterns
     * @return this
     * @see PatternFilterable Pattern Format
     */
    val setExcludes: Set<String>?

    context(Project)
    fun applyTo(receiver: T) {
        receiver::include trySet includes
        receiver::setIncludes trySet setIncludes
        receiver::exclude trySet excludes
        receiver::setExcludes trySet setExcludes
    }
}

@Serializable
internal data class PatternFilterableImpl(
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
) : PatternFilterable<org.gradle.api.tasks.util.PatternFilterable>
