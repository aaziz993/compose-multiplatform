package gradle.plugins.kotlin

import gradle.plugins.kotlin.model.LanguageSettings
import gradle.reflect.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.LanguageSettingsBuilder

/**
 * Represents most common Kotlin compilation settings for an entity.
 *
 * **Note**: This interface will be deprecated in the future.
 * Instead, it's better to use the existing `compilerOptions` DSL.
 *
 * See also [Compiler options DSL documentation](https://kotlinlang.org/docs/gradle-compiler-options.html).
 */
@Serializable
internal data class LanguageSettingsBuilder(
    /**
     * Provide source compatibility with the specified version of Kotlin.
     *
     * Possible values: "1.4 (deprecated)", "1.5 (deprecated)", "1.6 (deprecated)", "1.7", "1.8", "1.9", "2.0", "2.1 (experimental)"
     *
     * Default value: `null`
     */
    override val languageVersion: String? = null,
    /**
     * Allow using declarations only from the specified version of bundled libraries.
     *
     * Possible values: "1.4 (deprecated)", "1.5 (deprecated)", "1.6 (deprecated)", "1.7", "1.8", "1.9", "2.0", "2.1 (experimental)"
     *
     * Default value: `null`
     */
    override val apiVersion: String? = null,
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
    override val progressiveMode: Boolean? = null,
    /**
     * @suppress
     */
    override val enabledLanguageFeatures: Set<String>? = null,
    /**
     * Enable API usages that require opt-in with an opt-in requirement marker with the given fully qualified name.
     *
     * Default value: emptyList<String>()
     */
    override val optIns: Set<String>? = null,
) : LanguageSettings<LanguageSettingsBuilder> {

    override fun applyTo(receiver: LanguageSettingsBuilder) {
        receiver::languageVersion trySet languageVersion
        receiver::apiVersion trySet apiVersion
        receiver::progressiveMode trySet progressiveMode
        receiver.enabledLanguageFeatures
        enabledLanguageFeatures?.forEach(receiver::enableLanguageFeature)
        optIns?.forEach(receiver::optIn)
    }
}
