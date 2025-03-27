package gradle.plugins.kotlin.targets.web.npm

import gradle.collection.act
import kotlinx.serialization.Serializable

@Serializable
internal data class NpmOverride(
    val path: String,
    val includedVersions: Set<String>? = null,
    val setIncludedVersions: Set<String>? = null,
    val excludedVersions: Set<String>? = null,
    val setExcludedVersions: Set<String>? = null,
) {

    fun toNpmOverride() = org.jetbrains.kotlin.gradle.targets.js.npm.NpmOverride(
        path,
    ).apply {
        this@NpmOverride.includedVersions?.let(includedVersions::addAll)
        this@NpmOverride.setIncludedVersions?.act(includedVersions::clear)?.let(includedVersions::addAll)
        this@NpmOverride.excludedVersions?.let(excludedVersions::addAll)
        this@NpmOverride.setExcludedVersions?.act(excludedVersions::clear)?.let(excludedVersions::addAll)
    }
}
