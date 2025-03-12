package gradle.plugins.spotless

import com.diffplug.gradle.spotless.FormatExtension
import kotlinx.serialization.Serializable

@Serializable
internal data class ClangFormatConfig(
    val version: String? = null,
    val pathToExe: String? = null,
    val style: String? = null,
){
    fun applyTo(format: FormatExtension.ClangFormatConfig){
        pathToExe?.let(format::pathToExe)
        style?.let(format::style)
    }
}
