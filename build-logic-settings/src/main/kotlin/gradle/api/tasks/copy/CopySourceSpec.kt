package gradle.api.tasks.copy

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.CopySourceSpec

/**
 * Specifies sources for a file copy.
 */
internal interface CopySourceSpec<T : CopySourceSpec> {

    /**
     * Specifies the source files or directories for a copy and creates a child `CopySpec`. The given source
     * path is evaluated as per [org.gradle.api.Project.files] .
     *
     * @param sourcePath Path to source for the copy
     * @param configureAction action for configuring the child CopySpec
     */
    val from: @Serializable(with = FromSerializer::class) Any?

    context(Project)
    @Suppress("UNCHECKED_CAST")
    fun applyTo(recipient: T) {
        when (val from = from) {
            is String -> recipient.from(from)
            is LinkedHashSet<*> -> recipient.from(*from.toTypedArray())
            is From -> recipient.from(from.sourcePath) {
                from.copySpec.applyTo(this)
            }

            else -> Unit
        }
    }
}
