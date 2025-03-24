package gradle.plugins.kotlin

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions

/**
 * DSL entity with the ability to configure Kotlin compiler options.
 */
internal interface HasConfigurableKotlinCompilerOptions<T : HasConfigurableKotlinCompilerOptions<CO>,
    CO : org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions> {

    /**
     * Represents the compiler options used by a Kotlin compilation process.
     *
     * This can be used to get the values of currently configured options or modify them.
     */
    val compilerOptions: KotlinCommonCompilerOptions<CO>?

    context(project: Project)
    fun applyTo(options: T) {
        compilerOptions?.applyTo(options.compilerOptions)
    }
}
