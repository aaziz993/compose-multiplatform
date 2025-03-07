package gradle.model.gradle.spotless

import com.diffplug.gradle.spotless.JavaExtension
import kotlinx.serialization.Serializable

@Serializable
internal data class PalantirJavaFormatConfig(
    val version: String? = null,
    var style: String? = null,
    var formatJavadoc: Boolean? = null
) {

    fun applyTo(format: JavaExtension.PalantirJavaFormatConfig) {
        style?.let(format::style)
        formatJavadoc?.let(format::formatJavadoc)
    }
}

