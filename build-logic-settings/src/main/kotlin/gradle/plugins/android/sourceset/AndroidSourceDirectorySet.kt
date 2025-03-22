package gradle.plugins.android.sourceset

import com.android.build.api.dsl.AndroidSourceDirectorySet
import gradle.api.ProjectNamed
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * An AndroidSourceDirectorySet represents a set of directory inputs for an Android project.
 */
@Serializable
internal data class AndroidSourceDirectorySet(
    override val name: String? = null,,
    /**
     * Allows to add source directories to this list. `Directories` provides `MutableList` style access to all
     * source directories that are added via list itself or variety `srcDirs` methods.
     *
     * Note that tasks or buildscript may add more directories, so you should not read this property
     * and use Variant API instead.
     */
    val directories: Set<String>? = null,
    val setDirectories: Set<String>? = null,
) : ProjectNamed<AndroidSourceDirectorySet> {

    context(Project)
    override fun applyTo(recipient: AndroidSourceDirectorySet) {
        directories?.toTypedArray()?.let(recipient::srcDirs)
        setDirectories?.let(recipient::setSrcDirs)
    }
}
