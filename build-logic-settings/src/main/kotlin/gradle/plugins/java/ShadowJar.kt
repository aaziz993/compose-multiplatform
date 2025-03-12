package gradle.plugins.java

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import gradle.id
import gradle.libs
import gradle.tasks.copy.CopySpecImpl
import gradle.tasks.Expand
import gradle.tasks.copy.FileCopyDetails
import gradle.tasks.FilesMatching
import gradle.tasks.copy.FromSpec
import gradle.tasks.copy.IntoSpec
import gradle.plugin
import gradle.plugins
import gradle.serialization.serializer.AnySerializer
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Named
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
    override val properties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
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
    override val expand: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val expandDetails: Expand? = null,
    override val includes: List<String>? = null,
    override val setIncludes: List<String>? = null,
    override val excludes: List<String>? = null,
    override val setExcludes: List<String>? = null,
    override val relocators: List<Relocator>? = null,
    override val configurations: List<List<String>>? = null,
    override val dependencyFilter: DependencyFilter? = null,
    override val enableRelocation: Boolean? = null,
    override val relocationPrefix: String? = null,
    override val minimize: Boolean? = null,
    override val dependencyFilterForMinimize: DependencyFilter? = null,
    override val mergeServiceFiles: Boolean? = null,
    override val mergeServiceFilesPath: String? = null,
    override val append: String? = null
) : Jar(), ShadowSpec {

    context(Project)
    override fun applyTo(named: Named) =
        pluginManager.withPlugin(settings.libs.plugins.plugin("shadow").id) {
            super<Jar>.applyTo(named)
            super<ShadowSpec>.applyTo(named as ShadowJar)
        }

    context(Project)
    override fun applyTo() = applyTo(tasks.withType<ShadowJar>())
}
