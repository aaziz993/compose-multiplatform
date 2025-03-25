package gradle.api.tasks.archive

import gradle.api.tasks.Expand
import gradle.api.tasks.FilesMatching
import gradle.api.tasks.applyTo
import gradle.api.tasks.copy.FileCopyDetails
import gradle.api.tasks.copy.FromContentPolymorphicSerializer
import gradle.api.tasks.copy.IntoContentPolymorphicSerializer
import gradle.api.tasks.copy.Rename
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.bundling.ZipEntryCompression
import org.gradle.kotlin.dsl.withType

/**
 * Assembles a ZIP archive.
 *
 * The default is to compress the contents of the zip.
 */
internal abstract class Zip<T : org.gradle.api.tasks.bundling.Zip> : AbstractArchiveTask<T>() {

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
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.filePermissions
        entryCompression?.let(receiver::setEntryCompression)
        allowZip64?.let(receiver::setZip64)
        metadataCharset?.let(receiver::setMetadataCharset)
    }
}

@Serializable
@SerialName("Zip")
internal data class ZipImpl(
    override val entryCompression: ZipEntryCompression? = null,
    override val allowZip64: Boolean? = null,
    override val metadataCharset: String? = null,
    override val archiveFileName: String? = null,
    override val destinationDirectory: String? = null,
    override val archiveBaseName: String? = null,
    override val archiveAppendix: String? = null,
    override val archiveVersion: String? = null,
    override val archiveExtension: String? = null,
    override val archiveClassifier: String? = null,
    override val preserveFileTimestamps: Boolean? = null,
    override val reproducibleFileOrder: Boolean? = null,
    override val caseSensitive: Boolean? = null,
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val isCaseSensitive: Boolean? = null,
    override val includeEmptyDirs: Boolean? = null,
    override val duplicatesStrategy: DuplicatesStrategy? = null,
    override val filesMatching: FilesMatching? = null,
    override val filesNotMatching: FilesMatching? = null,
    override val filteringCharset: String? = null,
    override val froms: LinkedHashSet<@Serializable(with = FromContentPolymorphicSerializer::class) Any>? = null,
    override val into: @Serializable(with = IntoContentPolymorphicSerializer::class) Any? = null,
    override val renames: Set<Rename>? = null,
    override val filePermissions: Int? = null,
    override val dirPermissions: Int? = null,
    override val eachFile: FileCopyDetails? = null,
    override val expand: SerializableAnyMap? = null,
    override val expandDetails: Expand? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    override val name: String? = null,
) : Zip<org.gradle.api.tasks.bundling.Zip>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.gradle.api.tasks.bundling.Zip>())
}
