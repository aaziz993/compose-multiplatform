package gradle.plugins.android

import kotlinx.serialization.Serializable

/** DSL object for configuring aapt options.  */
@Serializable
internal data class AaptOptions(
    override val additionalParameters: List<String>? = null,
    override val noCompress: List<String>? = null,
    override val failOnMissingConfigEntry: Boolean? = null,
    override val ignoreAssetsPattern: String? = null,
    override val ignoreAssetsPatterns: List<String>? = null,
    override val namespaced: Boolean? = null,
) : AndroidResources
