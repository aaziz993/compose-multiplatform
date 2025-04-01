package gradle.plugins.kotlin.targets.web.npm

import klib.data.type.collection.tryAddAll
import klib.data.type.collection.trySet
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
        includedVersions tryAddAll this@NpmOverride.includedVersions
        includedVersions trySet this@NpmOverride.setIncludedVersions
        excludedVersions tryAddAll this@NpmOverride.excludedVersions
        excludedVersions trySet this@NpmOverride.setExcludedVersions
    }
}
