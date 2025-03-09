package gradle.model.kotlin.kmp.apple

import gradle.serialization.serializer.AnySerializer
import gradle.trySet
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
    override val productInfo: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val productModuleName: String? = null,
    override val productName: String? = null,
    val launchStoryboard: String? = null,
    val mainStoryboard: String? = null,
    val multipleWindows: Boolean? = null,
    val sceneDelegateClass: String? = null,
    val orientations: OrientationsHandler? = null,
    val sceneConfigurations: List<SceneConfiguration>? = null,
) : AppleTarget {

    override fun applyTo(target: org.jetbrains.gradle.apple.targets.AppleTarget) {
        super.applyTo(target)

        target as IosAppTarget

        target::launchStoryboard trySet launchStoryboard
        target::mainStoryboard trySet mainStoryboard
        target::multipleWindows trySet multipleWindows
        target::sceneDelegateClass trySet sceneDelegateClass

        orientations?.let { orientations ->
            target.orientations(orientations::applyTo)
        }

        sceneConfigurations?.forEach { sceneConfigurations ->
            target.sceneConfiguration(sceneConfigurations.name, sceneConfigurations::applyTo)
        }
    }
}
