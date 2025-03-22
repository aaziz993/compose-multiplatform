package gradle.plugins.kmp.nat

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

@Serializable
internal data class TestExecutableSettings(
    override val name: String? = null,,
    override val baseName: String? = null,
    override val debuggable: Boolean? = null,
    override val optimized: Boolean? = null,
    override val linkerOpts: List<String>? = null,
    override val binaryOptions: Map<String, String>? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    val namePrefix: String = "",
    val buildTypes: Set<NativeBuildType> = NativeBuildType.DEFAULT_BUILD_TYPES,
) : TestExecutable()
