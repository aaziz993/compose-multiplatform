package gradle.tasks.copy

import gradle.serialization.serializer.AnySerializer
import gradle.tasks.Expand
import gradle.tasks.FilesMatching
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.withType

/**
 * Copies files into a destination directory. This task can also rename and filter files as it copies. The task
 * implements [CopySpec][org.gradle.api.file.CopySpec] for specifying what to copy.
 *
 *
 *  Examples:
 * <pre class='autoTested'>
 * task copyDocs(type: Copy) {
 * from 'src/main/doc'
 * into 'build/target/doc'
 * }
 *
 * //for Ant filter
 * import org.apache.tools.ant.filters.ReplaceTokens
 *
 * //for including in the copy task
 * def dataContent = copySpec {
 * from 'src/data'
 * include '*.data'
 * }
 *
 * task initConfig(type: Copy) {
 * from('src/main/config') {
 * include '**&#47;*.properties'
 * include '**&#47;*.xml'
 * filter(ReplaceTokens, tokens: [version: '2.3.1'])
 * }
 * from('src/main/config') {
 * exclude '**&#47;*.properties', '**&#47;*.xml'
 * }
 * from('src/main/languages') {
 * rename 'EN_US_(.*)', '$1'
 * }
 * into 'build/target/config'
 * exclude '**&#47;*.bak'
 *
 * includeEmptyDirs = false
 *
 * with dataContent
 * }
</pre> *
 */
internal abstract class Copy : AbstractCopyTask() {

    /**
     * Sets the directory to copy files into. This is the same as calling [.into] on this task.
     *
     * @param destinationDir The destination directory. Must not be null.
     */
    abstract val destinationDir: String?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.gradle.api.tasks.Copy

        destinationDir?.let(::file)?.let(named::setDestinationDir)
    }

    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<org.gradle.api.tasks.Copy>()) { name ->
            tasks.register(name).get()
        }
}

@Serializable
@SerialName("Copy")
internal data class CopyImpl(
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
    override val destinationDir: String? = null,
) : Copy()
