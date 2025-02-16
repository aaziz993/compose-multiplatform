package plugin.project.gradle.spotless.model

import com.diffplug.spotless.cpp.ClangFormatStep
import kotlinx.serialization.Serializable

@Serializable
internal data class ClangFormatConfig(
    val version: String? = null,
    val pathToExe: String? = null,
    val style: String? = null,
)
