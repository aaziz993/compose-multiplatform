package gradle.plugins.kmp.jvm

import gradle.plugins.kotlin.KotlinCommonCompilerOptions
import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

/**
 * Compiler options for Kotlin/JVM.
 */
@Serializable
internal data class KotlinJvmCompilerOptions(
    override val apiVersion: KotlinVersion? = null,
    override val languageVersion: KotlinVersion? = null,
    override val optIns: List<String>? = null,
    override val progressiveMode: Boolean? = null,
    override val allWarningsAsErrors: Boolean? = null,
    override val extraWarnings: Boolean? = null,
    override val suppressWarnings: Boolean? = null,
    override val verbose: Boolean? = null,
    override val freeCompilerArgs: List<String>? = null,
    /**
     * Generate metadata for Java 1.8 reflection on method parameters.
     *
     * Default value: false
     */
    val javaParameters: Boolean? = null,
    /**
     * The target version of the generated JVM bytecode (1.8 and 9–23), with 1.8 as the default.
     *
     * Possible values: "1.8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"
     *
     * Default value: JvmTarget.DEFAULT
     */
    val jvmTarget: JvmTarget? = null,
    /**
     * Name of the generated '.kotlin_module' file.
     *
     * Default value: null
     */
    val moduleName: String? = null,
    /**
     * Don't automatically include the Java runtime in the classpath.
     *
     * Default value: false
     */
    val noJdk: Boolean? = null,
) : KotlinCommonCompilerOptions {

    context(Project)
    override fun applyTo(options: org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions) {
        super.applyTo(options)

        options as KotlinJvmCompilerOptions

        options.javaParameters tryAssign javaParameters
        options.jvmTarget tryAssign jvmTarget
        options.moduleName tryAssign moduleName
        options.noJdk tryAssign noJdk
    }
}
