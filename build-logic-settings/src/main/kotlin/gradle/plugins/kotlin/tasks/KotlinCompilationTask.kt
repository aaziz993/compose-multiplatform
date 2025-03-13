package gradle.plugins.kotlin.tasks

import gradle.plugins.kotlin.KotlinCommonCompilerOptions
import gradle.api.tasks.Task
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

/**
 * Represents a Kotlin task compiling using configurable [compilerOptions].
 *
 * See [gradle.plugins.kotlin.KotlinCommonCompilerOptions] and its inheritors for possible compiler options.
 *
 * @see [gradle.plugins.kotlin.KotlinCommonCompilerOptions]
 */
internal interface KotlinCompilationTask<out CO : KotlinCommonCompilerOptions> : Task {

    /**
     * Represents the compiler options used by a Kotlin compilation process.
     *
     * This can be used to get the values of currently configured options or modify them.
     */
    val compilerOptions: CO?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as KotlinCompilationTask<*>

        compilerOptions?.applyTo(named.compilerOptions)
    }

    context(Project)
    override fun applyTo() = applyTo(tasks.withType<KotlinCompilationTask<*>>())
}
