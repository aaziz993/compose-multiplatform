package gradle.plugins.kotlin.ksp

import com.google.devtools.ksp.gradle.KspAATask
import com.google.devtools.ksp.gradle.KspTask
import com.google.devtools.ksp.gradle.KspTaskJS
import com.google.devtools.ksp.gradle.KspTaskJvm
import com.google.devtools.ksp.gradle.KspTaskMetadata
import com.google.devtools.ksp.gradle.KspTaskNative
import gradle.accessors.ksp
import gradle.api.tryAssign
import gradle.api.trySet
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
    fun applyTo() {
        KspTask
        KspTaskJS
        KspTaskJvm
        KspTaskNative
        KspAATask
        KspTaskMetadata
        ksp.useKsp2 tryAssign useKsp2
        commandLineArgumentProviders?.forEach(ksp::arg)
        excludedProcessors?.forEach(ksp::excludeProcessor)
        excludedSources?.toTypedArray()?.let(ksp.excludedSources::from)
        setExcludedSources?.let(ksp.excludedSources::setFrom)
        arguments?.forEach { (key, value) -> ksp.arg(key, value) }
        ksp::allWarningsAsErrors trySet allWarningsAsErrors
    }
}
