package gradle.plugins.kotlin.targets.nat.tasks

import gradle.accessors.moduleName
import gradle.plugins.kotlin.KotlinCommonCompilerOptions
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
    override val setOptIns: List<String>? = null,
    override val progressiveMode: Boolean? = null,
    override val allWarningsAsErrors: Boolean? = null,
    override val extraWarnings: Boolean? = null,
    override val suppressWarnings: Boolean? = null,
    override val verbose: Boolean? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val setFreeCompilerArgs: List<String>? = null,
    override val apiVersion: KotlinVersion? = null,
    /**
     * Specify a name for the compilation module.
     *
     * Default value: null
     */
    val moduleName: String? = null,
) : KotlinCommonCompilerOptions<KotlinNativeCompilerOptions> {

    context(Project)
    override fun applyTo(receiver: KotlinNativeCompilerOptions) {
        super.applyTo(receiver)

        receiver.moduleName.assign(moduleName ?: project.moduleName)
    }
}
