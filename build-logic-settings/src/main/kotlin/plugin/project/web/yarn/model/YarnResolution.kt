package plugin.project.web.yarn.model

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnResolution

@Serializable
internal data class YarnResolution(
    val path: String,
    val includedVersions: List<String>? = null,
    val excludedVersions: List<String>? = null,
){
    fun toYarnResolution() =  YarnResolution(path).also {
        includedVersions?.let(it.includedVersions::addAll)
        excludedVersions?.let(it.excludedVersions::addAll)
    }
}
