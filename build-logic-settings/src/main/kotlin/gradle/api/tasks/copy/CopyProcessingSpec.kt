package gradle.api.tasks.copy

import gradle.api.file.ContentFilterable
import org.gradle.api.Project
import org.gradle.api.file.CopyProcessingSpec

/**
 * Specifies the destination of a copy.
 */
internal interface CopyProcessingSpec<T : CopyProcessingSpec> : ContentFilterable<T> {

    /**
     * Specifies the destination directory for a copy. The destination is evaluated as per [ ][org.gradle.api.Project.file].
     *
     * @param destPath Path to the destination directory for a Copy
     * @return this
     */
    val into: Any?

    /**
     * Renames files based on a regular expression.  Uses java.util.regex type of regular expressions.  Note that the
     * replace string should use the '$1' syntax to refer to capture groups in the source regular expression.  Files
     * that do not match the source regular expression will be copied with the original name.
     *
     *
     *  Example:
     * <pre>
     * rename '(.*)_OEM_BLUE_(.*)', '$1$2'
    </pre> *
     * would map the file 'style_OEM_BLUE_.css' to 'style.css'
     *
     * @param sourceRegEx Source regular expression
     * @param replaceWith Replacement string (use $ syntax for capture groups)
     * @return this
     */
    val renames: Set<Rename>?

    /**
     * Configuration action for specifying file access permissions.
     * For details see [ConfigurableFilePermissions].
     *
     * @since 8.3
     */
    val filePermissions: Int?

    /**
     * Configuration action for specifying directory access permissions.
     * For details see [ConfigurableFilePermissions].
     *
     * @since 8.3
     */
    val dirPermissions: Int?

    /**
     * Adds an action to be applied to each file as it is about to be copied into its destination. The action can change
     * the destination path of the file, filter the contents of the file, or exclude the file from the result entirely.
     * Actions are executed in the order added, and are inherited from the parent spec.
     *
     * @param action The action to execute.
     * @return this
     */
    val eachFile: FileCopyDetails?

    context(project: Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        into?.takeIf { into -> into is String }?.let(receiver::into)

        renames?.forEach { (sourceRegEx, replaceWith) -> receiver.rename(sourceRegEx, replaceWith) }

        filePermissions?.let { filePermissions ->
            receiver.filePermissions {
                unix(filePermissions)
            }
        }

        dirPermissions?.let { dirPermissions ->
            receiver.dirPermissions {
                unix(dirPermissions)
            }
        }

        eachFile?.let { eachFile ->
            receiver.eachFile(eachFile::applyTo)
        }
    }
}
