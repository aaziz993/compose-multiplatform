package gradle.api.tasks.copy

import gradle.accessors.files

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
    val froms: LinkedHashSet<@Serializable(with = FromContentPolymorphicSerializer::class) Any>?

    context(Project)
    @Suppress("UNCHECKED_CAST")
    fun applyTo(receiver: T) {
        froms?.filterIsInstance<String>()?.toTypedArray()?.let(receiver::from)

        froms?.filterIsInstance<From>()?.forEach { (sourcePath, copySpec) ->
            receiver.from(sourcePath) {
                copySpec.applyTo(this)
            }
        }
    }
}
