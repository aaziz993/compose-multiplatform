package gradle.plugins.java.tasks.shadow

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import gradle.accessors.files
import gradle.api.tasks.Expand
import gradle.api.tasks.FilesMatching
import gradle.api.tasks.applyTo
import gradle.api.tasks.copy.CopySpecImpl
import gradle.api.tasks.copy.FileCopyDetails
import gradle.api.tasks.copy.FromContentPolymorphicSerializer
import gradle.api.tasks.copy.IntoContentPolymorphicSerializer
import gradle.api.tasks.copy.Rename
import klib.data.type.serialization.json.serializer.SerializableAnyMap
import gradle.plugins.java.manifest.Manifest
import gradle.plugins.java.tasks.Jar
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.bundling.ZipEntryCompression
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class ShadowJar(
    override val manifestContentCharset: String? = null,
    override val manifest: Manifest? = null,
    override val metaInf: CopySpecImpl? = null,
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
    override val relocators: Set<Relocator>? = null,
    override val dependencies: DependencyFilter? = null,
    override val minimize: @Serializable(with = DependencyFilterContentPolymorphicSerializer::class) Any? = null,
    override val mergeServiceFiles: @Serializable(with = MergeServiceFilesContentPolymorphicSerializer::class) Any? = null,
    override val append: String? = null,
    val configurations: List<Set<String>>? = null,
    val enableRelocation: Boolean? = null,
    val relocationPrefix: String? = null,
) : Jar<ShadowJar>(), ShadowSpec<ShadowJar> {

    context(Project)
    override fun applyTo(receiver: ShadowJar) =
        project.pluginManager.withPlugin("com.gradleup.shadow") {
            super<Jar>.applyTo(receiver)
            super<ShadowSpec>.applyTo(receiver)

            receiver::setConfigurations trySet configurations?.map(project::files)
            receiver::setEnableRelocation trySet enableRelocation
            receiver::setRelocationPrefix trySet relocationPrefix
        }

    context(Project)
    override fun applyTo() = applyTo(project.tasks.withType<ShadowJar>())
}
