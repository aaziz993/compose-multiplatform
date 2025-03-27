package gradle.plugins.kotlin.targets.web.yarn

import gradle.act
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnResolution

@Serializable
internal data class YarnResolution(
    val path: String,
    val includedVersions: List<String>? = null,
    val setIncludedVersions: List<String>? = null,
    val excludedVersions: List<String>? = null,
    val setExcludedVersions: List<String>? = null,
) {

    fun toYarnResolution() = YarnResolution(path).apply {
        this@YarnResolution.includedVersions?.let(includedVersions::addAll)
        this@YarnResolution.setIncludedVersions?.act(includedVersions::clear)?.let(includedVersions::addAll)
        this@YarnResolution.excludedVersions?.let(excludedVersions::addAll)
        this@YarnResolution.setExcludedVersions?.act(excludedVersions::clear)?.let(excludedVersions::addAll)
    }
}
