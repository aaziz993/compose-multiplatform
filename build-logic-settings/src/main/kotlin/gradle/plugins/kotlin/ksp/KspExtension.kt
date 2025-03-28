package gradle.plugins.kotlin.ksp

import gradle.accessors.id
import gradle.accessors.ksp
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.tryAssign
import gradle.api.tryFrom
import gradle.api.trySet
import gradle.api.trySetFrom
import gradle.process.CommandLineArgumentProvider
import org.gradle.api.Project

internal interface KspExtension {

    /**
     * Enables or disables KSP 2, defaults to the `ksp.useKsp2` gradle property or `false` if that's not set.
     *
     * This API is temporary and will be removed once KSP1 is removed.
     */
    val useKsp2: Boolean?
    val commandLineArgumentProviders: List<CommandLineArgumentProvider>?
    val excludedProcessors: Set<String>?

    // Specify sources that should be excluded from KSP.
    // If you have a task that generates sources, you can call `ksp.excludedSources.from(task)`.
    val excludedSources: Set<String>?

    val setExcludedSources: Set<String>?

    val arguments: Map<String, String>?

    // Treat all warning as errors.
    val allWarningsAsErrors: Boolean?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("ksp").id) {
            project.ksp.useKsp2 tryAssign useKsp2
            commandLineArgumentProviders?.forEach(project.ksp::arg)
            excludedProcessors?.forEach(project.ksp::excludeProcessor)
            project.ksp.excludedSources tryFrom excludedSources
            project.ksp.excludedSources trySetFrom setExcludedSources
            arguments?.forEach { (key, value) -> project.ksp.arg(key, value) }
            project.ksp::allWarningsAsErrors trySet allWarningsAsErrors
        }
}
