@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.java

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings

import gradle.api.tasks.Expand
import gradle.api.tasks.FilesMatching
import gradle.api.tasks.applyTo
import gradle.api.tasks.copy.CopySpecImpl
import gradle.api.tasks.copy.FileCopyDetails
import gradle.api.tasks.copy.FromSpec
import gradle.api.tasks.copy.IntoSpec
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.bundling.ZipEntryCompression
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class ShadowJar(
    override var manifestContentCharset: String? = null,
    override var manifest: Manifest? = null,
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
    override val setExcludes: List<String>? = null,
    override val relocators: List<Relocator>? = null,
    val configurations: List<List<String>>? = null,
    override val dependencyFilter: DependencyFilter? = null,
    val enableRelocation: Boolean? = null,
    val relocationPrefix: String? = null,
    override val minimize: Boolean? = null,
    override val dependencyFilterForMinimize: DependencyFilter? = null,
    override val mergeServiceFiles: Boolean? = null,
    override val mergeServiceFilesPath: String? = null,
    override val append: String? = null
) : Jar<ShadowJar>(), ShadowSpec<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowSpec> {

    context(Project)
    override fun applyTo(recipient: ShadowJar) =
        pluginManager.withPlugin(settings.libs.plugins.plugin("shadow").id) {
            super<Jar>.applyTo(recipient)
            super<ShadowSpec>.applyTo(recipient)
            configurations?.map { files(*it.toTypedArray()) }?.let(recipient::setConfigurations)
            enableRelocation?.let(recipient::setEnableRelocation)
            relocationPrefix?.let(recipient::setRelocationPrefix)
        }

    context(Project)
    override fun applyTo() = applyTo(tasks.withType<ShadowJar>())
}
