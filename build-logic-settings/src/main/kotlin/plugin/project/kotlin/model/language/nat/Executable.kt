package plugin.project.kotlin.model.language.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.Executable
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

@Serializable
internal data class Executable(
    /**
     * The fully qualified name of the main function. For an example:
     *
     * - "main"
     * - "com.example.main"
     *
     *  The main function can either take no arguments or an Array<String>.
     */
    val entryPoint: String? = null,
    val buildTypes: List<NativeBuildType>? = null,
) : NativeBinary {

    context(Project)
    fun applyTo(executable: Executable) {
        super.applyTo(executable)
        entryPoint?.let(executable::entryPoint)
    }
}
