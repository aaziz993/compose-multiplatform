package plugin.project.kotlinnative.model

import gradle.tryAssign
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework

@Serializable
internal data class Framework(
    override val baseName: String? = null,
    override val transitiveExport: Boolean? = null,
    override val debuggable: Boolean? = null,
    override val optimized: Boolean? = null,
    override val linkerOpts: List<String>? = null,
    override val binaryOptions: Map<String, String>? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    val isStatic: Boolean? = null,
) : AbstractNativeLibrary {

    context(Project)
    fun applyTo(framework: Framework) {
        framework::baseName trySet baseName
        framework::transitiveExport trySet transitiveExport
        framework::debuggable trySet debuggable
        framework::optimized trySet optimized
        linkerOpts?.let(framework::linkerOpts)
        framework::binaryOptions trySet binaryOptions?.toMutableMap()
        framework::freeCompilerArgs trySet freeCompilerArgs
        framework::outputDirectory trySet optimized?.let(::file)
        framework.outputDirectoryProperty tryAssign outputDirectoryProperty?.let(layout.projectDirectory::dir)
        framework::isStatic trySet isStatic
    }
}
