package gradle.api.tasks.copy

import gradle.api.tasks.Expand
import gradle.api.tasks.FilesMatching
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import gradle.reflect.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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
internal abstract class Copy<T : org.gradle.api.tasks.Copy> : AbstractCopyTask<T>() {

    /**
     * Sets the directory to copy files into. This is the same as calling [.into] on this task.
     *
     * @param destinationDir The destination directory. Must not be null.
     */
    abstract val destinationDir: String?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::setDestinationDir trySet destinationDir?.let(project::file)
    }
}

@Serializable
@SerialName("Copy")
internal data class CopyImpl(
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
    override val destinationDir: String? = null,
) : Copy<org.gradle.api.tasks.Copy>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.gradle.api.tasks.Copy>())
}
