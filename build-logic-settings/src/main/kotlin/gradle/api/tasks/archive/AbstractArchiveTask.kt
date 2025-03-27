package gradle.api.tasks.archive

import gradle.api.tasks.Expand
import gradle.api.tasks.FilesMatching
import gradle.api.tasks.applyTo
import gradle.api.tasks.copy.AbstractCopyTask
import gradle.api.tasks.copy.FileCopyDetails
import gradle.api.tasks.copy.FromContentPolymorphicSerializer
import gradle.api.tasks.copy.IntoContentPolymorphicSerializer
import gradle.api.tasks.copy.Rename
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.withType

/**
 * `AbstractArchiveTask` is the base class for all archive tasks.
 */
internal abstract class AbstractArchiveTask<T : org.gradle.api.tasks.bundling.AbstractArchiveTask> : AbstractCopyTask<T>() {

    /**
     * Returns the archive name. If the name has not been explicitly set, the pattern for the name is:
     * `[archiveBaseName]-[archiveAppendix]-[archiveVersion]-[archiveClassifier].[archiveExtension]`
     *
     * @return the archive name.
     * @since 5.1
     */
    abstract val archiveFileName: String?

    /**
     * The directory where the archive will be placed.
     *
     * @since 5.1
     */
    abstract val destinationDirectory: String?

    /**
     * Returns the base name of the archive.
     *
     * @return the base name. Internal property may be null.
     * @since 5.1
     */
    abstract val archiveBaseName: String?

    /**
     * Returns the appendix part of the archive name, if any.
     *
     * @return the appendix. May be null
     * @since 5.1
     */
    abstract val archiveAppendix: String?

    /**
     * Returns the version part of the archive name.
     *
     * @return the version. Internal property may be null.
     * @since 5.1
     */
    abstract val archiveVersion: String?

    /**
     * Returns the extension part of the archive name.
     *
     * @since 5.1
     */
    abstract val archiveExtension: String?

    /**
     * Returns the classifier part of the archive name, if any.
     *
     * @return The classifier. Internal property may be null.
     * @since 5.1
     */
    abstract val archiveClassifier: String?

    /**
     * Specifies whether file timestamps should be preserved in the archive.
     *
     *
     * If `false` this ensures that archive entries have the same time for builds between different machines, Java versions and operating systems.
     *
     *
     * @param preserveFileTimestamps `true` if file timestamps should be preserved for archive entries
     * @since 3.4
     */
    abstract val preserveFileTimestamps: Boolean?

    /**
     * Specifies whether to enforce a reproducible file order when reading files from directories.
     *
     *
     * Gradle will then walk the directories on disk which are part of this archive in a reproducible order
     * independent of file systems and operating systems.
     * This helps Gradle reliably produce byte-for-byte reproducible archives.
     *
     *
     * @param reproducibleFileOrder `true` if the files should read from disk in a reproducible order.
     * @since 3.4
     */
    abstract val reproducibleFileOrder: Boolean?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.archiveBaseName tryAssign archiveBaseName
        receiver.destinationDirectory tryAssign destinationDirectory?.let(project.layout.projectDirectory::dir)
        receiver.archiveFileName tryAssign archiveFileName
        receiver.archiveAppendix tryAssign archiveAppendix
        receiver.archiveVersion tryAssign archiveVersion
        receiver.archiveExtension tryAssign archiveExtension
        receiver.archiveClassifier tryAssign archiveClassifier
        receiver::setPreserveFileTimestamps trySet preserveFileTimestamps
        receiver::setReproducibleFileOrder trySet reproducibleFileOrder
    }
}

@Serializable
@SerialName("AbstractArchiveTask")
internal data class AbstractArchiveTaskImpl(
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
) : AbstractArchiveTask<org.gradle.api.tasks.bundling.AbstractArchiveTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.gradle.api.tasks.bundling.AbstractArchiveTask>())
}
