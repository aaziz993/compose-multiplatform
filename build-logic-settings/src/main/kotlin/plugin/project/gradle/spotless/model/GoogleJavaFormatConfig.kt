package plugin.project.gradle.spotless.model

import com.diffplug.spotless.java.GoogleJavaFormatStep
import kotlinx.serialization.Serializable

@Serializable
internal data class GoogleJavaFormatConfig(
    val version: String? = null,
    val groupArtifact: String? = null,
    val style: String? = null,
    val reflowLongStrings: Boolean? = null,
    val reorderImports: Boolean? = null,
    val formatJavadoc: Boolean? = null,
)

