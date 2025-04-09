package gradle.plugins.apple.target

import klib.data.type.serialization.serializer.SerializableAnyMap
import gradle.plugins.apple.AppleBuildSettings
import gradle.plugins.apple.BuildConfiguration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.gradle.apple.targets.IosFrameworkTarget

@Serializable
@SerialName("iosFramework")
internal data class IosFrameworkTarget(
    override val bridgingHeader: String? = null,
    override val buildConfigurations: LinkedHashSet<BuildConfiguration>? = null,
    override val buildSettings: Map<AppleBuildSettings, String>? = null,
    override val embedFrameworks: Boolean? = null,
    override val ipad: Boolean? = null,
    override val iphone: Boolean? = null,
    override val name: String? = null,
    override val productInfo: SerializableAnyMap? = null,
    override val setProductInfo: SerializableAnyMap? = null,
    override val productModuleName: String? = null,
    override val productName: String? = null
) : AppleTarget<IosFrameworkTarget>
