package gradle.plugins.apple

import gradle.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.gradle.apple.dsl.SceneConfiguration

@Serializable
internal data class SceneConfiguration(
    val className: String? = null,
    val delegateClassName: String? = null,
    val name: String,
    val storyboardName: String? = null

) {

    fun applyTo(configuration: SceneConfiguration) {
        configuration::className trySet className
        configuration::delegateClassName trySet delegateClassName
        configuration::storyboardName trySet storyboardName
    }
}
