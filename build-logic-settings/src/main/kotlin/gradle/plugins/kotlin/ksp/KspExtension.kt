package gradle.plugins.kotlin.ksp

import gradle.accessors.ksp
import gradle.api.file.tryFrom
import gradle.api.file.trySetFrom
import gradle.api.provider.tryAssign
import gradle.process.CommandLineArgumentProvider
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class KspExtension(
    /**
     * Enables or disables KSP 2, defaults to the `ksp.useKsp2` gradle property or `false` if that's not set.
     *
     * This API is temporary and will be removed once KSP1 is removed.
     */
    val useKsp2: Boolean? = null,
    val commandLineArgumentProviders: List<CommandLineArgumentProvider>? = null,
    val excludedProcessors: Set<String>? = null,
    // Specify sources that should be excluded from KSP.
    // If you have a task that generates sources, you can call `ksp.excludedSources.from(task)`.
    val excludedSources: Set<String>? = null,
    val setExcludedSources: Set<String>? = null,
    val arguments: Map<String, String>? = null,
    // Treat all warning as errors.
    val allWarningsAsErrors: Boolean? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("com.google.devtools.ksp") {
            project.ksp.useKsp2 tryAssign useKsp2
            commandLineArgumentProviders?.forEach(project.ksp::arg)
            excludedProcessors?.forEach(project.ksp::excludeProcessor)
            project.ksp.excludedSources tryFrom excludedSources
            project.ksp.excludedSources trySetFrom setExcludedSources
            arguments?.forEach { (key, value) -> project.ksp.arg(key, value) }
            project.ksp::allWarningsAsErrors trySet allWarningsAsErrors
        }
}
