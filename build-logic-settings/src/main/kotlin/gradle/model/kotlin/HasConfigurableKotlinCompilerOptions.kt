package gradle.model.kotlin

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions

/**
 * DSL entity with the ability to configure Kotlin compiler options.
 */
internal interface HasConfigurableKotlinCompilerOptions<CO : KotlinCommonCompilerOptions> {

    /**
     * Represents the compiler options used by a Kotlin compilation process.
     *
     * This can be used to get the values of currently configured options or modify them.
     */
    val compilerOptions: CO?

    context(Project)
    fun applyTo(options: HasConfigurableKotlinCompilerOptions<*>) {
        compilerOptions?.applyTo(options.compilerOptions)
    }
}
