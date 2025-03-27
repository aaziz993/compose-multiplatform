package gradle.plugins.apple.target

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
        landscapeLeft?.takeIfTrue()?.act(receiver::landscapeLeft)
        landscapeRight?.takeIfTrue()?.act(receiver::landscapeRight)
        portrait?.takeIfTrue()?.act(receiver::portrait)
        portraitUpsideDown?.takeIfTrue()?.act(receiver::portraitUpsideDown)
    }
}
