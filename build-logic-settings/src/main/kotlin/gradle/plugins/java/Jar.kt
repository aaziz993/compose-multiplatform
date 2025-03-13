package gradle.plugins.java

import gradle.accessors.projectProperties
import gradle.plugins.kmp.jvm.KotlinJvmTarget
import gradle.collection.SerializableAnyMap
import gradle.api.tasks.Expand
import gradle.api.tasks.FilesMatching
import gradle.api.tasks.archive.Zip
import gradle.api.tasks.copy.CopySpec
import gradle.api.tasks.copy.FileCopyDetails
import gradle.api.tasks.copy.FromSpec
import gradle.api.tasks.copy.IntoSpec
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.bundling.ZipEntryCompression
import org.gradle.kotlin.dsl.withType

/**
 * Assembles a JAR archive.
 */
internal abstract class Jar : Zip() {

    /**
     * The character set used to encode the manifest content.
     *
     * @param manifestContentCharset the character set used to encode the manifest content
     * @see .getManifestContentCharset
     * @since 2.14
     */
    abstract var manifestContentCharset: String?

    /**
     * Sets the manifest for this JAR archive.
     *
     * @param manifest The manifest. May be null.
     */
    abstract var manifest: Manifest?

    /**
     * Adds content to this JAR archive's META-INF directory.
     *
     *
     * The given action is executed to configure a `CopySpec`.
     *
     * @param configureAction The action.
     * @return The created `CopySpec`
     * @since 3.5
     */

    abstract val metaInf: CopySpec?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.gradle.api.tasks.bundling.Jar

        metadataCharset?.let(named::setMetadataCharset)
        manifestContentCharset?.let(named::setManifestContentCharset)
        manifest?.applyTo(named.manifest)
        metaInf?.applyTo(named.metaInf)
    }

    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<org.gradle.api.tasks.bundling.Jar>()) { name ->
            if (projectProperties.kotlin.targets.any { target -> target is KotlinJvmTarget })
                tasks.register(name).get()
            else null
        }
}

@Serializable
internal data class JarImpl(
    override var manifestContentCharset: String? = null,
    override var manifest: Manifest? = null,
    override val metaInf: CopySpec? = null,
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
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = "",
    override val isCaseSensitive: Boolean? = null,
    override val includeEmptyDirs: Boolean? = null,
    override val duplicatesStrategy: DuplicatesStrategy? = null,
    override val filesMatching: FilesMatching? = null,
    override val filesNotMatching: FilesMatching? = null,
    override val filteringCharset: String? = null,
    override val from: List<String>? = null,
    override val fromSpec: FromSpec? = null,
    override val into: String? = null,
    override val intoSpec: IntoSpec? = null,
    override val rename: Map<String, String>? = null,
    override val renamePattern: Map<String, String>? = null,
    override val filePermissions: Int? = null,
    override val dirPermissions: Int? = null,
    override val eachFile: FileCopyDetails? = null,
    override val expand: SerializableAnyMap? = null,
    override val expandDetails: Expand? = null,
    override val includes: List<String>? = null,
    override val setIncludes: List<String>? = null,
    override val excludes: List<String>? = null,
    override val setExcludes: List<String>?
) : Jar()
