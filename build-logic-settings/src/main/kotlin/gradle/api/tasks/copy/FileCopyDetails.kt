package gradle.api.tasks.copy

import gradle.api.file.RelativePath
import kotlinx.serialization.Serializable
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.internal.file.DefaultFilePermissions

/**
 *
 * Provides details about a file or directory about to be copied, and allows some aspects of the destination file to
 * be modified.
 *
 *
 * Using this interface, you can change the destination path of the file, filter the content of the file, or exclude
 * the file from the result entirely.
 *
 *
 * Access to the source file itself after any filters have been added is not a supported operation.
 *
 *
 */
@Serializable
internal data class FileCopyDetails(

    /**
     * Excludes this file from the copy.
     */
    val exclude: Boolean? = null,

    /**
     * Sets the destination path of this file.
     *
     * @param path The path of this file.
     */
    val path: String? = null,

    /**
     * Sets the destination path of this file.
     *
     * @param path the new path for this file.
     */
    val relativePath: RelativePath? = null,

    /**
     * Set file and directory access permissions based on an externally
     * provided permission instance.
     * For details see [org.gradle.api.file.ConfigurableFilePermissions].
     *
     * @since 8.3
     */
    val permissions: Int? = null,

    /**
     * The strategy to use if there is already a file at this file's destination.
     */
    val duplicatesStrategy: DuplicatesStrategy? = null,

    /**
     * Sets the destination name of this file.
     *
     * @param name The destination name of this file.
     */
    val name: String? = null,
) {

    fun applyTo(receiver: FileCopyDetails) {
        exclude?.takeIfTrue()?.act(receiver::exclude)
        path?.let(receiver::setPath)
        relativePath?.let(RelativePath::toRelativePath)?.let(receiver::setRelativePath)
        permissions?.let(::DefaultFilePermissions)?.let(receiver::setPermissions)
        duplicatesStrategy?.let(receiver::setDuplicatesStrategy)
        name?.let(receiver::setName)
    }
}
