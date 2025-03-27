package gradle.plugins.kotlin.targets.web.npm

import gradle.act
import gradle.api.tryAddAll
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
        this@NpmOverride.includedVersions tryAddAll includedVersions
        this@NpmOverride.includedVersions tryAddAll setIncludedVersions
        this@NpmOverride.excludedVersions tryAddAll excludedVersions
        this@NpmOverride.excludedVersions tryAddAll setExcludedVersions
    }
}
