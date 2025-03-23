package gradle.api.file

import gradle.api.ProjectNamed
import gradle.api.tasks.util.PatternFilterable
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.SourceDirectorySet

/**
 * A `SourceDirectorySet` represents a set of source files composed from a set of source directories, along
 * with associated include and exclude patterns.
 *
 *
 * `SourceDirectorySet` extends [FileTree]. The contents of the file tree represent the source files of this set,
 * arranged in a hierarchy. The file tree is live and reflects changes to the source directories and their contents.
 *
 *
 * You can create an instance of `SourceDirectorySet` using the [org.gradle.api.model.ObjectFactory.sourceDirectorySet]
 * method.
 *
 *
 * You can filter the **files** that are obtainable in this set using patterns via [.include]
 * and [.include] (or any overload of these methods).  The set of included source directories themselves are
 * **not filtered**.
 */
@Serializable
internal data class SourceDirectorySet(
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    override val name: String? = null,
    /**
     * Adds the given source directories to this set. The given directories do not need to exist. Directories that do not exist are ignored.
     *
     * @param srcPaths The source directories. These are evaluated as per [org.gradle.api.Project.files]
     * @return this
     */
    val srcDirs: List<String>? = null,
    /**
     * Sets the source directories for this set.
     *
     * @param srcPaths The source directories. These are evaluated as per [org.gradle.api.Project.files]
     * @return this
     */
    val setSrcDirs: List<String>? = null,
    /**
     * Configure the directory to assemble the compiled classes into.
     *
     * @return The destination directory property for this set of sources.
     * @since 6.1
     */
    val destinationDirectory: String? = null,
) : PatternFilterable<SourceDirectorySet>, ProjectNamed<SourceDirectorySet> {

    context(Project)
    override fun applyTo(receiver: SourceDirectorySet) {
        super<PatternFilterable>.applyTo(receiver)

        srcDirs?.toTypedArray()?.let(receiver::srcDirs)
        setSrcDirs?.let(receiver::setSrcDirs)
        receiver.destinationDirectory tryAssign destinationDirectory?.let(layout.projectDirectory::dir)
    }
}
