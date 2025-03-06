package gradle.model

import org.gradle.api.file.CopySourceSpec

/**
 * Specifies sources for a file copy.
 */
internal interface CopySourceSpec {

    /**
     * Specifies source files or directories for a copy. The given paths are evaluated as per [ ][org.gradle.api.Project.files].
     *
     * @param sourcePaths Paths to source files for the copy
     */
    val from: List<String>?

    /**
     * Specifies the source files or directories for a copy and creates a child `CopySpec`. The given source
     * path is evaluated as per [org.gradle.api.Project.files] .
     *
     * @param sourcePath Path to source for the copy
     * @param configureAction action for configuring the child CopySpec
     */
    val fromSpec: FromSpec?

    fun applyTo(spec: CopySourceSpec) {
        from?.let { from -> spec.from(*from.toTypedArray()) }
        fromSpec?.let { fromSpec ->
            spec.from(fromSpec.sourcePath) {
                fromSpec.copySpec
            }
        }
    }
}


