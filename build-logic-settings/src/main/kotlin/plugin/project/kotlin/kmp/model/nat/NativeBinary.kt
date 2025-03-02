package plugin.project.kotlin.kmp.model.nat

import gradle.moduleName
import gradle.tryAssign
import gradle.trySet
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary
import plugin.project.kotlin.model.Named
import plugin.project.kotlin.model.configure

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
    fun applyTo(binaries: NamedDomainObjectCollection<out NativeBinary>) =
        binaries.configure {
            baseName = this@NativeBinary.baseName ?: moduleName
            ::debuggable trySet this@NativeBinary.debuggable
            ::optimized trySet this@NativeBinary.optimized
            this@NativeBinary.linkerOpts?.let(::linkerOpts)
            ::binaryOptions trySet this@NativeBinary.binaryOptions?.toMutableMap()
            ::freeCompilerArgs trySet this@NativeBinary.freeCompilerArgs
            ::outputDirectory trySet this@NativeBinary.optimized?.let(::file)
            outputDirectoryProperty tryAssign this@NativeBinary.outputDirectoryProperty?.let(layout.projectDirectory::dir)
        }
}
