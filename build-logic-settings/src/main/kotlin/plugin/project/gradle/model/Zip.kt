package plugin.project.gradle.model

import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip
import org.gradle.api.tasks.bundling.ZipEntryCompression

/**
 * Assembles a ZIP archive.
 *
 * The default is to compress the contents of the zip.
 */
@Serializable
internal abstract class Zip : AbstractArchiveTask() {

    /**
     * Sets the compression level of the entries of the archive. If set to [ZipEntryCompression.DEFLATED] (the default), each entry is
     * compressed using the DEFLATE algorithm. If set to [ZipEntryCompression.STORED] the entries of the archive are left uncompressed.
     *
     * @param entryCompression `STORED` or `DEFLATED`
     */
    abstract val entryCompression: ZipEntryCompression?

    /**
     * Enables building zips with more than 65535 files or bigger than 4GB.
     *
     * @see .isZip64
     */
    abstract val allowZip64: Boolean?

    /**
     * The character set used to encode ZIP metadata like file names.
     * Defaults to the platform's default character set.
     *
     * @param metadataCharset the character set used to encode ZIP metadata like file names
     * @since 2.14
     */
    abstract val metadataCharset: String?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as Zip

        named.filePermissions
        entryCompression?.let(named::setEntryCompression)
        allowZip64?.let(named::setZip64)
        metadataCharset?.let(named::setMetadataCharset)
    }
}
