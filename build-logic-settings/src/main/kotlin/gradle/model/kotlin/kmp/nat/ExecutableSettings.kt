package gradle.model.kotlin.kmp.nat

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

@Serializable
internal data class ExecutableSettings(
    override val name: String = "",
    override val baseName: String? = null,
    override val debuggable: Boolean? = null,
    override val optimized: Boolean? = null,
    override val linkerOpts: List<String>? = null,
    override val binaryOptions: Map<String, String>? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    override val entryPoint: String? = null,
    val buildTypes: List<NativeBuildType>? = null,
) : Executable()
