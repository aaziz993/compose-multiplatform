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
        landscapeLeft?.takeIf { it }?.run { receiver.landscapeLeft() }
        landscapeRight?.takeIf { it }?.run { receiver.landscapeRight() }
        portrait?.takeIf { it }?.run { receiver.portrait() }
        portraitUpsideDown?.takeIf { it }?.run { receiver.portraitUpsideDown() }
    }
}
