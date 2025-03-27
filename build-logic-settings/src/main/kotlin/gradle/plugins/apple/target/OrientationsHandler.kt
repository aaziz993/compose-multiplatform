package gradle.plugins.apple.target

import gradle.actIfTrue
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
        landscapeLeft?.actIfTrue(receiver::landscapeLeft)
        landscapeRight?.actIfTrue(receiver::landscapeRight)
        portrait?.actIfTrue(receiver::portrait)
        portraitUpsideDown?.actIfTrue(receiver::portraitUpsideDown)
    }
}
