package plugin.project.kotlin.kmp.model.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable

@Serializable
internal data class TestExecutable(
    override val baseName: String? = null,
    override val debuggable: Boolean? = null,
    override val optimized: Boolean? = null,
    override val linkerOpts: List<String>? = null,
    override val binaryOptions: Map<String, String>? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    val buildTypes: List<NativeBuildType>? = null,
) : NativeBinary {

    context(Project)
    fun applyTo(executable: TestExecutable) {
        super.applyTo(executable)
    }
}
