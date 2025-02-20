package plugin.project.kotlin.model.language

import gradle.tryAssign

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
}

internal fun org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions.configureFrom(config: KotlinCommonCompilerToolOptions) {
    allWarningsAsErrors tryAssign config.allWarningsAsErrors
    extraWarnings tryAssign config.extraWarnings
    suppressWarnings tryAssign config.suppressWarnings
    verbose tryAssign config.verbose
    freeCompilerArgs tryAssign config.freeCompilerArgs
}
