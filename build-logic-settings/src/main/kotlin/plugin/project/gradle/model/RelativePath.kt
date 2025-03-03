package plugin.project.gradle.model

import kotlinx.serialization.Serializable
import org.gradle.api.file.RelativePath

@Serializable
internal data class RelativePath(
    val endsWithFile: Boolean,
    val segments: List<String>? = null
) {

    fun toRelativePath() = RelativePath(
        endsWithFile,
        *segments.orEmpty().toTypedArray(),
    )
}
