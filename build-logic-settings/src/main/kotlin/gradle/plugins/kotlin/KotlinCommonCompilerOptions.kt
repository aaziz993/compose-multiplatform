package gradle.plugins.kotlin

import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.act
import gradle.api.tryAddAll
import gradle.api.tryAssign
import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

/**
 * Common compiler options for all Kotlin platforms.
 */
internal interface KotlinCommonCompilerOptions<T : org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions>
    : KotlinCommonCompilerToolOptions<T> {

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
    val setOptIns: List<String>?

    /**
     * Enable progressive compiler mode. In this mode, deprecations and bug fixes for unstable code take effect immediately instead of going through a graceful migration cycle. Code written in progressive mode is backward compatible; however, code written without progressive mode enabled may cause compilation errors in progressive mode.
     *
     * Default value: false
     */

    val progressiveMode: Boolean?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.apiVersion tryAssign (apiVersion ?: project.settings.libs.versions.version("kotlin.apiVersion")
            ?.let(KotlinVersion::valueOf))
        receiver.languageVersion tryAssign (languageVersion
            ?: project.settings.libs.versions.version("kotlin.languageVersion")
                ?.let(KotlinVersion::valueOf))
        receiver.optIn tryAddAll optIns
        receiver.optIn trySet setOptIns
        receiver.progressiveMode tryAssign progressiveMode
    }
}

@Serializable
internal data class KotlinCommonCompilerOptionsImpl(
    override val apiVersion: KotlinVersion? = null,
    override val languageVersion: KotlinVersion? = null,
    override val optIns: List<String>? = null,
    override val setOptIns: List<String>? = null,
    override val progressiveMode: Boolean? = null,
    override val allWarningsAsErrors: Boolean? = null,
    override val extraWarnings: Boolean? = null,
    override val suppressWarnings: Boolean? = null,
    override val verbose: Boolean? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val setFreeCompilerArgs: List<String>? = null,
) : KotlinCommonCompilerOptions<org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions>
