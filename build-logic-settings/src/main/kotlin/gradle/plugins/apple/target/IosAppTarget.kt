package gradle.plugins.apple.target

import gradle.collection.SerializableAnyMap
import gradle.plugins.apple.AppleBuildSettings
import gradle.plugins.apple.BuildConfiguration
import klib.data.type.reflection.tryApply
import klib.data.type.reflection.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.gradle.apple.targets.IosAppTarget

@Serializable
@SerialName("iosApp")
internal data class IosAppTarget(
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
    override val productName: String? = null,
    val launchStoryboard: String? = null,
    val mainStoryboard: String? = null,
    val multipleWindows: Boolean? = null,
    val sceneDelegateClass: String? = null,
    val orientations: OrientationsHandler? = null,
    val sceneConfigurations: List<SceneConfiguration>? = null,
) : AppleTarget<IosAppTarget> {

    context(Project)
    override fun applyTo(receiver: IosAppTarget) {
        super.applyTo(receiver)

        receiver::launchStoryboard trySet launchStoryboard
        receiver::mainStoryboard trySet mainStoryboard
        receiver::multipleWindows trySet multipleWindows
        receiver::sceneDelegateClass trySet sceneDelegateClass
        receiver::orientations tryApply orientations?.let { orientations -> orientations::applyTo }

        sceneConfigurations?.forEach { sceneConfigurations ->
            receiver.sceneConfiguration(sceneConfigurations.name, sceneConfigurations::applyTo)
        }
    }
}
