package plugin.project.kotlin.model.language

import gradle.tryAssign
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions

/**
 * Common options for all Kotlin platforms' compilations and tools.
 */
internal interface KotlinCommonCompilerToolOptions {

    /**
     * Report an error if there are any warnings.
     *
     * Default value: false
     */

    val allWarningsAsErrors: Boolean?

    /**
     * Enable extra checkers for K2.
     *
     * Default value: false
     */

    val extraWarnings: Boolean?

    /**
     * Don't generate any warnings.
     *
     * Default value: false
     */

    val suppressWarnings: Boolean?

    /**
     * Enable verbose logging output.
     *
     * Default value: false
     */

    val verbose: Boolean?

    /**
     * A list of additional compiler arguments
     *
     * Default value: emptyList<String>()
     */

    val freeCompilerArgs: List<String>?

    fun applyTo(compilerToolOptions: KotlinCommonCompilerToolOptions) {
        compilerToolOptions.allWarningsAsErrors tryAssign allWarningsAsErrors
        compilerToolOptions.extraWarnings tryAssign extraWarnings
        compilerToolOptions.suppressWarnings tryAssign suppressWarnings
        compilerToolOptions.verbose tryAssign verbose
        compilerToolOptions.freeCompilerArgs tryAssign freeCompilerArgs
    }
}
