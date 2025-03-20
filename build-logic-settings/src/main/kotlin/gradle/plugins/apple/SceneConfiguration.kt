package gradle.plugins.apple

import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.gradle.apple.dsl.SceneConfiguration

@Serializable
internal data class SceneConfiguration(
    val className: String? = null,
    val delegateClassName: String? = null,
    val name: String,
    val storyboardName: String? = null

) {

    fun applyTo(recipient: SceneConfiguration) {
        recipient::className trySet className
        recipient::delegateClassName trySet delegateClassName
        recipient::storyboardName trySet storyboardName
    }
}
