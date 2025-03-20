package gradle.plugins.kotlin

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilationOutput

/**
 * Represents the outputs of a Kotlin source set compilation.
 */
@Serializable
internal data class KotlinCompilationOutput(
    /**
     * The collection of directories where the compiled code is located.
     *
     * For example, in the case of JVM target compilation,
     * this will be directories containing class files for Java and Kotlin sources compilations.
     */
    val classesDirs: List<String>? = null,
) {

    fun applyTo(recipient: KotlinCompilationOutput) {
        classesDirs?.let(output.classesDirs::setFrom)
    }
}
