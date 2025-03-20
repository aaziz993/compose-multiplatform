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
        landscapeLeft?.takeIf { it }?.run { handler.landscapeLeft() }
        landscapeRight?.takeIf { it }?.run { handler.landscapeRight() }
        portrait?.takeIf { it }?.run { handler.portrait() }
        portraitUpsideDown?.takeIf { it }?.run { handler.portraitUpsideDown() }
    }
}
