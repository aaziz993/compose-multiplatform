package gradle.plugins.kotlin

import gradle.api.trySet
import org.jetbrains.kotlin.gradle.plugin.LanguageSettingsBuilder

/**
 * Represents most common Kotlin compilation settings for an entity.
 *
 * **Note**: This interface will be deprecated in the future.
 * Instead, it's better to use the existing `compilerOptions` DSL.
 *
 * See also [Compiler options DSL documentation](https://kotlinlang.org/docs/gradle-compiler-options.html).
 */
internal interface LanguageSettingsBuilder {

    /**
     * Provide source compatibility with the specified version of Kotlin.
     *
     * Possible values: "1.4 (deprecated)", "1.5 (deprecated)", "1.6 (deprecated)", "1.7", "1.8", "1.9", "2.0", "2.1 (experimental)"
     *
     * Default value: `null`
     */
    val languageVersion: String?

    /**
     * Allow using declarations only from the specified version of bundled libraries.
     *
     * Possible values: "1.4 (deprecated)", "1.5 (deprecated)", "1.6 (deprecated)", "1.7", "1.8", "1.9", "2.0", "2.1 (experimental)"
     *
     * Default value: `null`
     */
    val apiVersion: String?

    /**
     * Enable progressive compiler mode.
     *
     * In this mode, deprecations and bug fixes for unstable code take effect immediately instead of
     * going through a graceful migration cycle.
     * Code written in progressive mode is backward compatible;
     * however, code written without progressive mode enabled may cause compilation errors
     * in progressive mode.
     *
     *  Default value: false
     */
    val progressiveMode: Boolean?

    /**
     * @suppress
     */
    val enabledLanguageFeatures: Set<String>?

    /**
     * Enable API usages that require opt-in with an opt-in requirement marker with the given fully qualified name.
     *
     * Default value: emptyList<String>()
     */
    val optIns: Set<String>?

    fun applyTo(builder: LanguageSettingsBuilder) {
        builder::languageVersion trySet languageVersion
        builder::apiVersion trySet apiVersion
        builder::progressiveMode trySet progressiveMode
        builder.enabledLanguageFeatures
        enabledLanguageFeatures?.forEach(builder::enableLanguageFeature)
        optIns?.forEach(builder::optIn)
    }
}
