package gradle.model.android.library

import gradle.model.android.AndroidResources
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring Android resource options for Library plugins.
 *
 * This is accessed via [LibraryExtension.androidResources]
 */
@Serializable
internal data class LibraryAndroidResources(
    override val ignoreAssetsPattern: String? = null,
    override val ignoreAssetsPatterns: List<String>? = null,
    override val noCompress: List<String>? = null,
    override val failOnMissingConfigEntry: Boolean? = null,
    override val additionalParameters: List<String>? = null,
    override val namespaced: Boolean? = null,
) : AndroidResources
