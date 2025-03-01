package plugin.project.kotlin.kmp.model.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.jetbrains.kotlin.gradle.dsl.KotlinNativeCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

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
    override val apiVersion: KotlinVersion?,
) : plugin.project.kotlin.model.KotlinCommonCompilerOptions {

    context(Project)
    fun applyTo(options: KotlinNativeCompilerOptions) {
        super.applyTo(options)

        options.moduleName.assign(moduleName ?: moduleName)
    }
}
