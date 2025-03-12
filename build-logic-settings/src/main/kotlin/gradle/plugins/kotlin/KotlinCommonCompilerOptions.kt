package gradle.plugins.kotlin

import gradle.libs
import gradle.settings
import gradle.tryAssign
import gradle.version
import gradle.versions
import org.gradle.api.Project
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

    val optIns: List<String>?

    /**
     * Enable progressive compiler mode. In this mode, deprecations and bug fixes for unstable code take effect immediately instead of going through a graceful migration cycle. Code written in progressive mode is backward compatible; however, code written without progressive mode enabled may cause compilation errors in progressive mode.
     *
     * Default value: false
     */

    val progressiveMode: Boolean?

    context(Project)
    override fun applyTo(options: org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions) {
        super.applyTo(options)

        options as KotlinCommonCompilerOptions

        options.apiVersion tryAssign (apiVersion ?: settings.libs.versions.version("kotlin.apiVersion")
            ?.let(KotlinVersion::valueOf))
        options.languageVersion tryAssign (languageVersion ?: settings.libs.versions.version("kotlin.languageVersion")
            ?.let(KotlinVersion::valueOf))
        optIns?.let(options.optIn::addAll)
        options.progressiveMode tryAssign progressiveMode
    }
}
