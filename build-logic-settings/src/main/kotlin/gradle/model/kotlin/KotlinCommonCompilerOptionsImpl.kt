package gradle.model.kotlin

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

@Serializable
internal data class KotlinCommonCompilerOptionsImpl(
    override val apiVersion: KotlinVersion? = null,
    override val languageVersion: KotlinVersion? = null,
    override val optIns: List<String>? = null,
    override val progressiveMode: Boolean? = null,
    override val allWarningsAsErrors: Boolean? = null,
    override val extraWarnings: Boolean? = null,
    override val suppressWarnings: Boolean? = null,
    override val verbose: Boolean? = null,
    override val freeCompilerArgs: List<String>? = null,
) : KotlinCommonCompilerOptions
