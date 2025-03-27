package gradle.plugins.apple.target

import gradle.ifTrue
import kotlinx.serialization.Serializable
import org.jetbrains.gradle.apple.dsl.OrientationsHandler

@Serializable
internal data class OrientationsHandler(
    val landscapeLeft: Boolean? = null,
    val landscapeRight: Boolean? = null,
    val portrait: Boolean? = null,
    val portraitUpsideDown: Boolean? = null,
) {

    fun applyTo(receiver: OrientationsHandler) {
        landscapeLeft?.ifTrue(receiver::landscapeLeft)
        landscapeRight?.ifTrue(receiver::landscapeRight)
        portrait?.ifTrue(receiver::portrait)
        portraitUpsideDown?.ifTrue(receiver::portraitUpsideDown)
    }
}
