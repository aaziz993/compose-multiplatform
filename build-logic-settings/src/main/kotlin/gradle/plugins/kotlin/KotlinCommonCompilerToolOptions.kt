package gradle.plugins.kotlin

import gradle.api.tryAssign
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions

/**
 * Common options for all Kotlin platforms' compilations and tools.
 */
internal interface KotlinCommonCompilerToolOptions<T : KotlinCommonCompilerToolOptions> {

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
    val setFreeCompilerArgs: List<String>?

    context(Project)
    fun applyTo(recipient: T) {
        recipient.allWarningsAsErrors tryAssign allWarningsAsErrors
        recipient.extraWarnings tryAssign extraWarnings
        recipient.suppressWarnings tryAssign suppressWarnings
        recipient.verbose tryAssign verbose

        recipient.freeCompilerArgs tryAssign freeCompilerArgs?.let { freeCompilerArgs ->
            recipient.freeCompilerArgs.get() + freeCompilerArgs
        }

        recipient.freeCompilerArgs tryAssign setFreeCompilerArgs
    }
}
