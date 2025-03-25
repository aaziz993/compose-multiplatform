package gradle.plugins.kmp.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerPluginData

@Serializable
internal data class KotlinCompilerPluginData(
    val classpath: List<String>,
    val options: CompilerPluginOptions,
    /**
     * Used only for Up-to-date checks
     */
    val inputsOutputsState: InputsOutputsState
) {

    context(Project)
    @OptIn(InternalKotlinGradlePluginApi::class)
    fun toKotlinCompilerPluginData() = KotlinCompilerPluginData(
        files(*classpath.toTypedArray()),
        options.toCompilerPluginOptions(),
        inputsOutputsState.toInputsOutputsState(),
    )

    @Serializable
    data class InputsOutputsState(
        val inputs: Map<String, String>,
        val inputFiles: Set<String>,
        val outputFiles: Set<String>
    ) {

        context(Project)
        @OptIn(InternalKotlinGradlePluginApi::class)
        fun toInputsOutputsState() = KotlinCompilerPluginData.InputsOutputsState(
            inputs,
            inputFiles.map(project::file).toSet(),
            outputFiles.map(project::file).toSet(),
        )
    }
}
