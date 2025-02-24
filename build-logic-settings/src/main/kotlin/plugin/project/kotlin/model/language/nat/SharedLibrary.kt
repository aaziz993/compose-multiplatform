package plugin.project.kotlin.model.language.nat

import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.plugin.mpp.SharedLibrary

@Serializable
internal data class SharedLibrary(
    override val baseName: String? = null,
    override val transitiveExport: Boolean? = null,
    override val debuggable: Boolean? = null,
    override val optimized: Boolean? = null,
    override val linkerOpts: List<String>? = null,
    override val binaryOptions: Map<String, String>? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    val buildTypes: List<NativeBuildType>? = null,
) : AbstractNativeLibrary {

    context(Project)
    fun applyTo(library: SharedLibrary) {
        super.applyTo(library)
    }
}
