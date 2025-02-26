package plugin.project.kotlin.model.language

import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

/**
 * Represents a Kotlin task compiling using configurable [compilerOptions].
 *
 * See [KotlinCommonCompilerOptions] and its inheritors for possible compiler options.
 *
 * @see [KotlinCommonCompilerOptions]
 */
internal interface KotlinCompilationTask<out CO : KotlinCommonCompilerOptions> : Task {

    /**
     * Represents the compiler options used by a Kotlin compilation process.
     *
     * This can be used to get the values of currently configured options or modify them.
     */
    val compilerOptions: CO

    fun applyTo(task: KotlinCompilationTask<*>) {
        compilerOptions.applyTo(task.compilerOptions)
    }
}
