package gradle.plugins.apple

import kotlinx.serialization.Serializable
import org.jetbrains.gradle.apple.dsl.OrientationsHandler

@Serializable
internal data class OrientationsHandler(
    val landscapeLeft: Boolean? = null,
    val landscapeRight: Boolean? = null,
    val portrait: Boolean? = null,
    val portraitUpsideDown: Boolean? = null,
) {

    fun applyTo(recipient: OrientationsHandler) {
        landscapeLeft?.takeIf { it }?.run { recipient.landscapeLeft() }
        landscapeRight?.takeIf { it }?.run { recipient.landscapeRight() }
        portrait?.takeIf { it }?.run { recipient.portrait() }
        portraitUpsideDown?.takeIf { it }?.run { recipient.portraitUpsideDown() }
    }
}
