package plugin.project.gradle.spotless.model

import com.diffplug.spotless.java.PalantirJavaFormatStep
import kotlinx.serialization.Serializable

@Serializable
internal data class PalantirJavaFormatConfig(
    val version: String? = null,
    var style: String? = null,
    var formatJavadoc: Boolean? = null
)

