package plugin.project.gradle.model

import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable
import org.gradle.api.file.DuplicatesStrategy

/**
 * A set of specifications for copying files.  This includes:
 *
 *
 *
 *  * source directories (multiples allowed)
 *
 *  * destination directory
 *
 *  * ANT like include patterns
 *
 *  * ANT like exclude patterns
 *
 *  * File relocating rules
 *
 *  * renaming rules
 *
 *  * content filters
 *
 *
 *
 * CopySpecs may be nested by passing a closure to one of the from methods.  The closure creates a child CopySpec and
 * delegates methods in the closure to the child. Child CopySpecs inherit any values specified in the parent. This
 * allows constructs like:
 * <pre class='autoTested'>
 * def myCopySpec = project.copySpec {
 * into('webroot')
 * exclude('**&#47;.data/ **')
 * from('src/main/webapp') {
 * include '**&#47;*.jsp'
 * }
 * from('src/main/js') {
 * include '**&#47;*.js'
 * }
 * }
</pre> *
 *
 * In this example, the `into` and `exclude` specifications at the root level are inherited by the
 * two child CopySpecs.
 *
 * Copy specs can be reused in other copy specs via [.with] method. This enables reuse of the copy spec instances.
 *
 * <pre class='autoTested'>
 * def contentSpec = copySpec {
 * from("content") {
 * include "**&#47;*.txt"
 * }
 * }
 *
 * task copy(type: Copy) {
 * into "$buildDir/copy"
 * with contentSpec
 * }
</pre> *
 *
 * @see org.gradle.api.tasks.Copy Copy Task
 *
 * @see org.gradle.api.Project.copy
 */
@Serializable
internal data class CopySpecImpl(
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
) : CopySpec
