package gradle.plugins.android.library

import com.android.build.api.dsl.LibraryAndroidResources
import gradle.plugins.android.AndroidResources
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring Android resource options for Library plugins.
 *
 * This is accessed via [LibraryExtension.androidResources]
 */
@Serializable
internal data class LibraryAndroidResources(
    override val ignoreAssetsPattern: String? = null,
    override val ignoreAssetsPatterns: Set<String>? = null,
    override val setIgnoreAssetsPatterns: Set<String>? = null,
    override val noCompress: Set<String>? = null,
    override val setNoCompress: Set<String>? = null,
    override val failOnMissingConfigEntry: Boolean? = null,
    override val additionalParameters: List<String>? = null,
    override val setAdditionalParameters: List<String>? = null,
    override val namespaced: Boolean? = null,
) : AndroidResources<LibraryAndroidResources>
