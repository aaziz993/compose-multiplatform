package gradle.plugins.apple

import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable

@Serializable
internal data class IosFrameworkTarget(
    override val bridgingHeader: String? = null,
    override val buildConfigurations: List<BuildConfiguration>? = null,
    override val buildSettings: Map<AppleBuildSettings, String>? = null,
    override val embedFrameworks: Boolean? = null,
    override val ipad: Boolean? = null,
    override val iphone: Boolean? = null,
    override val name: String = "",
    override val productInfo: SerializableAnyMap? = null,
    override val productModuleName: String? = null,
    override val productName: String? = null
) : AppleTarget
