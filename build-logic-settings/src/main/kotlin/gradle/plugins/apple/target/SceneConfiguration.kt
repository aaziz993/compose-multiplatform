package gradle.plugins.apple.target

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

    fun applyTo(receiver: SceneConfiguration) {
        receiver::className trySet className
        receiver::delegateClassName trySet delegateClassName
        receiver::storyboardName trySet storyboardName
    }
}
