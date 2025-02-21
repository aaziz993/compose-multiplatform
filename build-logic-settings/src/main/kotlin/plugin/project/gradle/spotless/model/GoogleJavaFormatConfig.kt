package plugin.project.gradle.spotless.model

import com.diffplug.gradle.spotless.JavaExtension
import kotlinx.serialization.Serializable

@Serializable
internal data class GoogleJavaFormatConfig(
    val version: String? = null,
    val groupArtifact: String? = null,
    val style: String? = null,
    val reflowLongStrings: Boolean? = null,
    val reorderImports: Boolean? = null,
    val formatJavadoc: Boolean? = null,
) {

    fun applyTo(format: JavaExtension.GoogleJavaFormatConfig) {
        groupArtifact?.let(format::groupArtifact)
        style?.let(format::style)
        reflowLongStrings?.let(format::reflowLongStrings)
        reorderImports?.let(format::reorderImports)
        formatJavadoc?.let(format::formatJavadoc)
    }
}

