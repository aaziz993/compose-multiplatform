package gradle.plugins.apple

import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.jetbrains.gradle.apple.targets.IosAppTarget

@Serializable
internal data class IosAppTarget(
    override val bridgingHeader: String? = null,
    override val buildConfigurations: List<BuildConfiguration>? = null,
    override val buildSettings: Map<AppleBuildSettings, String>? = null,
    override val embedFrameworks: Boolean? = null,
    override val ipad: Boolean? = null,
    override val iphone: Boolean? = null,
    override val name: String = "",
    override val productInfo: SerializableAnyMap? = null,
    override val productModuleName: String? = null,
    override val productName: String? = null,
    val launchStoryboard: String? = null,
    val mainStoryboard: String? = null,
    val multipleWindows: Boolean? = null,
    val sceneDelegateClass: String? = null,
    val orientations: OrientationsHandler? = null,
    val sceneConfigurations: List<SceneConfiguration>? = null,
) : AppleTarget<IosAppTarget> {

    override fun applyTo(recipient: IosAppTarget) {
        super.applyTo(recipient)

        recipient::launchStoryboard trySet launchStoryboard
        recipient::mainStoryboard trySet mainStoryboard
        recipient::multipleWindows trySet multipleWindows
        recipient::sceneDelegateClass trySet sceneDelegateClass

        orientations?.let { orientations ->
            recipient.orientations(orientations::applyTo)
        }

        sceneConfigurations?.forEach { sceneConfigurations ->
            recipient.sceneConfiguration(sceneConfigurations.name, sceneConfigurations::applyTo)
        }
    }
}
