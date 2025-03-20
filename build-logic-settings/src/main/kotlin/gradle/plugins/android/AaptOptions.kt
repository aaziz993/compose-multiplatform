package gradle.plugins.android

import com.android.build.gradle.internal.dsl.AaptOptions
import kotlinx.serialization.Serializable

/** DSL object for configuring aapt options.  */
@Serializable
internal data class AaptOptions(
    override val additionalParameters: List<String>? = null,
    override val setAdditionalParameters: List<String>? = null,
    override val noCompress: Set<String>? = null,
    override val setNoCompress: Set<String>? = null,
    override val failOnMissingConfigEntry: Boolean? = null,
    override val ignoreAssetsPattern: String? = null,
    override val ignoreAssetsPatterns: Set<String>? = null,
    override val setIgnoreAssetsPatterns: Set<String>? = null,
    override val namespaced: Boolean? = null,
) : AndroidResources<AaptOptions>
