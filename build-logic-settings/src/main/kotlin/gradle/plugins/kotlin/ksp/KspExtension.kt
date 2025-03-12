package gradle.plugins.kotlin.ksp

import gradle.ksp
import gradle.tryAssign
import gradle.trySet
import org.gradle.api.Project

internal interface KspExtension {
    /**
     * Enables or disables KSP 2, defaults to the `ksp.useKsp2` gradle property or `false` if that's not set.
     *
     * This API is temporary and will be removed once KSP1 is removed.
     */
    val useKsp2: Boolean?
    val commandLineArgumentProviders: List<String>?
    val excludedProcessors: List<String>?
    // Specify sources that should be excluded from KSP.
    // If you have a task that generates sources, you can call `ksp.excludedSources.from(task)`.
    val excludedSources: List<String>?
    val arguments: Map<String, String>?
    // Treat all warning as errors.
    val allWarningsAsErrors: Boolean?

    context(Project)
    fun applyTo(){
        ksp.useKsp2 tryAssign useKsp2

        commandLineArgumentProviders?.let { commandLineArgumentProviders ->
            ksp.arg { commandLineArgumentProviders }
        }

        excludedProcessors?.forEach(ksp::excludeProcessor)
        excludedSources?.let(ksp.excludedSources::setFrom)
        arguments?.forEach { (key, value) -> ksp.arg(key, value) }
        ksp::allWarningsAsErrors trySet allWarningsAsErrors
    }
}
