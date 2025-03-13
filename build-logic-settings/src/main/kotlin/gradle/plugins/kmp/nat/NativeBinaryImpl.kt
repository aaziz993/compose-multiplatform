package gradle.plugins.kmp.nat

import kotlinx.serialization.Serializable

@Serializable
internal data class NativeBinaryImpl(
    override val baseName: String? = null,
    override val debuggable: Boolean? = null,
    override val optimized: Boolean? = null,
    override val linkerOpts: List<String>? = null,
    override val binaryOptions: Map<String, String>? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    override val name: String = ""
) : NativeBinary
