package gradle.api.tasks

import gradle.api.tasks.copy.AbstractCopyTask
import gradle.api.tasks.copy.FileCopyDetails
import gradle.api.tasks.copy.FromContentPolymorphicSerializer
import gradle.api.tasks.copy.IntoContentPolymorphicSerializer
import gradle.api.tasks.copy.Rename
import gradle.api.tasks.util.PatternFilterableImpl
import klib.data.type.collection.SerializableAnyMap
import klib.data.type.reflection.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.withType

internal abstract class Sync<T : org.gradle.api.tasks.Sync> : AbstractCopyTask<T>() {

    abstract val destinationDir: String?

    abstract val preserve: PatternFilterableImpl?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::setDestinationDir trySet destinationDir?.let(project::file)
        preserve?.applyTo(receiver)
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
    override val name: String? = null,
    override val isCaseSensitive: Boolean? = null,
    override val includeEmptyDirs: Boolean? = null,
    override val duplicatesStrategy: DuplicatesStrategy? = null,
    override val filesMatching: FilesMatching? = null,
    override val filesNotMatching: FilesMatching? = null,
    override val filteringCharset: String? = null,
    override val from: @Serializable(with = FromContentPolymorphicSerializer::class) Any? = null,
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
) : Sync<org.gradle.api.tasks.Sync>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.gradle.api.tasks.Sync>())
}
