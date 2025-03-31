package gradle.plugins.kotlin.targets.web.yarn

import gradle.collection.tryAddAll
import gradle.collection.trySet
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
        includedVersions tryAddAll this@YarnResolution.includedVersions
        includedVersions trySet this@YarnResolution.setIncludedVersions
        excludedVersions tryAddAll this@YarnResolution.excludedVersions
        excludedVersions trySet this@YarnResolution.setExcludedVersions
    }
}
