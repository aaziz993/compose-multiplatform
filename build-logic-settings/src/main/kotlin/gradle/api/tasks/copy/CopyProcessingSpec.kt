package gradle.api.tasks.copy

import gradle.tasks.ContentFilterable
import java.util.regex.Pattern
import org.gradle.api.file.CopyProcessingSpec

/**
 * Specifies the destination of a copy.
 */
internal interface CopyProcessingSpec : ContentFilterable {

    /**
     * Specifies the destination directory for a copy. The destination is evaluated as per [ ][org.gradle.api.Project.file].
     *
     * @param destPath Path to the destination directory for a Copy
     * @return this
     */
    val into: String?

    /**
     * Creates and configures a child `CopySpec` with a destination directory *inside* the archive for the files.
     * The destination is evaluated as per [org.gradle.api.Project.file].
     * Don't mix it up with [.getDestinationDirectory] which specifies the output directory for the archive.
     *
     * @param destPath destination directory *inside* the archive for the files
     * @param copySpec The closure to use to configure the child `CopySpec`.
     * @return this
     */
    val intoSpec: IntoSpec?

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
    val rename: Map<String, String>?

    /**
     * Renames files based on a regular expression. See [.rename].
     *
     * @param sourceRegEx Source regular expression
     * @param replaceWith Replacement string (use $ syntax for capture groups)
     * @return this
     */
    val renamePattern: Map<String, String>?

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

    override fun applyTo(filterable: org.gradle.api.file.ContentFilterable) {
        super.applyTo(filterable)

        filterable as CopyProcessingSpec

        into?.let(filterable::into)
        rename?.forEach(filterable::rename)
        renamePattern?.forEach { (key, value) -> filterable.rename(Pattern.compile(key), value) }

        filePermissions?.let { filePermissions ->
            filterable.filePermissions {
                unix(filePermissions)
            }
        }

        dirPermissions?.let { dirPermissions ->
            filterable.dirPermissions {
                unix(dirPermissions)
            }
        }

        eachFile?.let { eachFile ->
            filterable.eachFile(eachFile::applyTo)
        }
    }
}
