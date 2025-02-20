package plugin.project.kotlinnative.model

import kotlinx.serialization.Serializable

@Serializable
internal data class FrameworkSettings(
    override val baseName: String? = null,
    override val transitiveExport: Boolean?,
    override val debuggable: Boolean?,
    override val optimized: Boolean?,
    override val linkerOpts: List<String>?,
    override val binaryOptions: Map<String, String>?,
    override val freeCompilerArgs: List<String>?,
    override val outputDirectory: String?,
    override val outputDirectoryProperty: String?,
    val isStatic: Boolean? = null,
) : AbstractNativeLibrary
