package gradle.model

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.AbstractArchiveTask

/**
 * `AbstractArchiveTask` is the base class for all archive tasks.
 */
@Serializable
internal abstract class AbstractArchiveTask : AbstractCopyTask() {

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
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as AbstractArchiveTask

        named.archiveBaseName tryAssign archiveBaseName
        named.destinationDirectory tryAssign destinationDirectory?.let(layout.projectDirectory::dir)
        named.archiveFileName tryAssign archiveFileName
        named.archiveAppendix tryAssign archiveAppendix
        named.archiveVersion tryAssign archiveVersion
        named.archiveExtension tryAssign archiveExtension
        named.archiveClassifier tryAssign archiveClassifier
        into?.let(named::into)

        intoSpec?.let { intoSpec ->
            named.into(intoSpec.destPath){
                intoSpec.copySpec.applyTo(this)
            }
        }

        preserveFileTimestamps?.let(named::setPreserveFileTimestamps)
        reproducibleFileOrder?.let(named::setReproducibleFileOrder)
    }
}
