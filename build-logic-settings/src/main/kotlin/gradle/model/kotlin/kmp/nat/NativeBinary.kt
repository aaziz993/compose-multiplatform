package gradle.model.kotlin.kmp.nat

import gradle.moduleName
import gradle.tryAssign
import gradle.trySet
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary
import gradle.model.Named

internal interface NativeBinary : Named {

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
    override fun applyTo(named: org.gradle.api.Named) {
        named as NativeBinary

        named.baseName = baseName ?: moduleName
        named::debuggable trySet debuggable
        named::optimized trySet optimized
        linkerOpts?.let(named::linkerOpts)
        named::binaryOptions trySet binaryOptions?.toMutableMap()
        named::freeCompilerArgs trySet freeCompilerArgs
        named::outputDirectory trySet optimized?.let(::file)
        named.outputDirectoryProperty tryAssign outputDirectoryProperty?.let(layout.projectDirectory::dir)
    }
}
