package gradle.model.kotlin.kmp.web.npm.model

import kotlinx.serialization.Serializable

@Serializable
internal data class NpmOverride(
    val path: String,
    val includedVersions: List<String>? = null,
    val excludedVersions: List<String>? = null,
) {

    fun toNpmOverride() = org.jetbrains.kotlin.gradle.targets.js.npm.NpmOverride(
        path,
    ).apply {
        this@NpmOverride.includedVersions?.let(includedVersions::addAll)
        this@NpmOverride.excludedVersions?.let(excludedVersions::addAll)
    }
}
