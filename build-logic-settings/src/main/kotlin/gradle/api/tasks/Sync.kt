package gradle.api.tasks

import org.gradle.kotlin.dsl.withType
import gradle.api.tasks.copy.AbstractCopyTask
import gradle.api.tasks.copy.FileCopyDetails
import gradle.api.tasks.copy.FromSpec
import gradle.api.tasks.copy.IntoSpec
import gradle.api.tasks.util.PatternFilterableImpl
import gradle.collection.SerializableAnyMap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy

internal abstract class Sync<T : org.gradle.api.tasks.Sync> : AbstractCopyTask<T>() {

    abstract val destinationDir: String?

    abstract val preserve: PatternFilterableImpl?

    context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        destinationDir?.let(::file)?.let(recipient::setDestinationDir)
        preserve?.applyTo(recipient)
    }
}

@Serializable
@SerialName("Sync")
internal data class SyncImpl(
    override val destinationDir: String? = null,
    override val preserve: PatternFilterableImpl? = null,
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
    override val name: String = "",
    override val isCaseSensitive: Boolean? = null,
    override val includeEmptyDirs: Boolean? = null,
    override val duplicatesStrategy: DuplicatesStrategy? = null,
    override val filesMatching: FilesMatching? = null,
    override val filesNotMatching: FilesMatching? = null,
    override val filteringCharset: String? = null,
    override val from: Set<String>? = null,
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
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
) : Sync<org.gradle.api.tasks.Sync>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.gradle.api.tasks.Sync>())
}
