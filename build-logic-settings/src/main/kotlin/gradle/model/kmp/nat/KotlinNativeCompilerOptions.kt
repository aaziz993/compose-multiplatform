package gradle.model.kmp.nat

import gradle.moduleName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinNativeCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import gradle.model.kotlin.KotlinCommonCompilerOptions

/**
 * Compiler options for Kotlin Native.
 */
@Serializable
internal data class KotlinNativeCompilerOptions(
    override val languageVersion: KotlinVersion? = null,
    override val optIns: List<String>? = null,
    override val progressiveMode: Boolean? = null,
    override val allWarningsAsErrors: Boolean? = null,
    override val extraWarnings: Boolean? = null,
    override val suppressWarnings: Boolean? = null,
    override val verbose: Boolean? = null,
    override val freeCompilerArgs: List<String>? = null,
    /**
     * Specify a name for the compilation module.
     *
     * Default value: null
     */
    val moduleName: String? = null,
    override val apiVersion: KotlinVersion? = null
) : KotlinCommonCompilerOptions {

    context(Project)
    override fun applyTo(options: KotlinCommonCompilerToolOptions) {
        super.applyTo(options)

        options as KotlinNativeCompilerOptions

        options.moduleName.assign(moduleName ?: project.moduleName)
    }
}
