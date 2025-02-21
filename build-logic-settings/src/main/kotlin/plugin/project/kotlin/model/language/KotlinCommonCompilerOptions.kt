package plugin.project.kotlin.model.language

import gradle.tryAssign
import org.gradle.api.provider.ListProperty
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

/**
 * Common compiler options for all Kotlin platforms.
 */
internal interface KotlinCommonCompilerOptions : KotlinCommonCompilerToolOptions {

    /**
     * Allow using declarations from only the specified version of bundled libraries.
     *
     * Possible values: "1.6 (deprecated)", "1.7 (deprecated)", "1.8", "1.9", "2.0", "2.1", "2.2 (experimental)"
     *
     * Default value: null
     */

    val apiVersion: KotlinVersion?

    /**
     * Provide source compatibility with the specified version of Kotlin.
     *
     * Possible values: "1.6 (deprecated)", "1.7 (deprecated)", "1.8", "1.9", "2.0", "2.1", "2.2 (experimental)"
     *
     * Default value: null
     */

    val languageVersion: KotlinVersion?

    /**
     * Enable API usages that require opt-in with an opt-in requirement marker with the given fully qualified name.
     *
     * Default value: emptyList<String>()
     */

    val optIn: List<String>?

    /**
     * Enable progressive compiler mode. In this mode, deprecations and bug fixes for unstable code take effect immediately instead of going through a graceful migration cycle. Code written in progressive mode is backward compatible; however, code written without progressive mode enabled may cause compilation errors in progressive mode.
     *
     * Default value: false
     */

    val progressiveMode: Boolean?

    fun applyTo(compilerOptions: KotlinCommonCompilerOptions) {
        (this as KotlinCommonCompilerToolOptions).applyTo(compilerOptions)
        compilerOptions.apiVersion tryAssign apiVersion
        compilerOptions.languageVersion tryAssign apiVersion
        optIn?.let(compilerOptions.optIn::addAll)
        compilerOptions.progressiveMode tryAssign progressiveMode
    }
}
