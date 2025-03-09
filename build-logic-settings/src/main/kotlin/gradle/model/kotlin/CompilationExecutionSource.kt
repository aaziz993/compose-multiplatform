package gradle.model.kotlin

import gradle.model.kotlin.kmp.nat.KotlinNativeCompilation
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.CompilationExecutionSource

/**
 * An execution source produced by a [compilation].
 *
 * @see [CompilationExecutionSourceSupport].
 */
internal interface CompilationExecutionSource : KotlinExecution.ExecutionSource {

    val compilation: KotlinNativeCompilation?

    context(Project)
    fun applyTo(source: CompilationExecutionSource<*>) {
        compilation?.applyTo(source.compilation)
    }
}
