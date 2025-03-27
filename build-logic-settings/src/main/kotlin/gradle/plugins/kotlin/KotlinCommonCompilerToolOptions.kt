package gradle.plugins.kotlin

import gradle.api.tryAddAll
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Common options for all Kotlin platforms' compilations and tools.
 */
internal interface KotlinCommonCompilerToolOptions<T : org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions> {

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
    fun applyTo(receiver: T) {
        receiver.allWarningsAsErrors tryAssign allWarningsAsErrors
        receiver.extraWarnings tryAssign extraWarnings
        receiver.suppressWarnings tryAssign suppressWarnings
        receiver.verbose tryAssign verbose
        receiver.freeCompilerArgs tryAddAll freeCompilerArgs
        receiver.freeCompilerArgs tryAssign setFreeCompilerArgs
    }
}

@Serializable
internal data class KotlinCommonCompilerToolOptionsImpl(
    override val allWarningsAsErrors: Boolean? = null,
    override val extraWarnings: Boolean? = null,
    override val suppressWarnings: Boolean? = null,
    override val verbose: Boolean? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val setFreeCompilerArgs: List<String>? = null,
) : KotlinCommonCompilerToolOptions<org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions>
