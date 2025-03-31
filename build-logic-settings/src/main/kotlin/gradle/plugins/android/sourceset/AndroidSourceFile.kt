package gradle.plugins.android.sourceset

import com.android.build.api.dsl.AndroidSourceFile
import gradle.api.ProjectNamed
import gradle.reflect.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * An AndroidSourceFile represents a single file input for an Android project.
 */
@Serializable
internal data class AndroidSourceFile(
    /**
     * A concise name for the source directory (typically used to identify it in a collection).
     */
    override val name: String? = null,
    /**
     * Sets the location of the file.
     *
     * @param srcPath The source directory. This is evaluated as [org.gradle.api.Project.file]
     *
     * This method has a return value for legacy reasons.
     */
    val srcFile: String?
) : ProjectNamed<AndroidSourceFile> {

    context(Project)
    override fun applyTo(receiver: AndroidSourceFile) {
        receiver::srcFile trySet srcFile
    }
}
