package plugin.project.android.model

import kotlinx.serialization.Serializable
import com.android.build.gradle.internal.dsl.AaptOptions

/** DSL object for configuring aapt options.  */
@Serializable
internal data class AaptOptions(
    override val additionalParameters: List<String>? = null,
    override val noCompress: List<String>? = null,
    override var failOnMissingConfigEntry: Boolean? = null,
    override var ignoreAssetsPattern: String? = null,
    override val ignoreAssetsPatterns: List<String>? = null,
    override var namespaced: Boolean? = null,
) : AndroidResources
