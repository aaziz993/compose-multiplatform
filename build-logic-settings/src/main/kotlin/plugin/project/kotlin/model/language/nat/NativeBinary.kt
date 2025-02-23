package plugin.project.kotlin.model.language.nat

import gradle.asModuleName
import gradle.tryAssign
import gradle.trySet
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary

internal interface NativeBinary {

    val baseName: String?

    // Configuration DSL.
    val debuggable: Boolean?
    val optimized: Boolean?

    /** Additional options passed to the linker by the Kotlin/Native compiler. */
    val linkerOpts: List<String>?

    val binaryOptions: Map<String, String>?

    /** Additional arguments passed to the Kotlin/Native compiler. */
    val freeCompilerArgs: List<String>?

    // Output access.
    // TODO: Provide output configurations and integrate them with Gradle Native.
    val outputDirectory: String?

    val outputDirectoryProperty: String?

    context(Project)
    fun applyTo(binary: NativeBinary) {
        binary.baseName = baseName ?: "$group.${name.asModuleName()}"
        binary::debuggable trySet debuggable
        binary::optimized trySet optimized
        linkerOpts?.let(binary::linkerOpts)
        binary::binaryOptions trySet binaryOptions?.toMutableMap()
        binary::freeCompilerArgs trySet freeCompilerArgs
        binary::outputDirectory trySet optimized?.let(::file)
        binary.outputDirectoryProperty tryAssign outputDirectoryProperty?.let(layout.projectDirectory::dir)
    }
}
