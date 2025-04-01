package gradle.plugins.apple.target

import klib.data.type.reflection.trySet
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
        receiver::landscapeLeft trySet landscapeLeft
        receiver::landscapeRight trySet landscapeRight
        receiver::portrait trySet portrait
        receiver::portraitUpsideDown trySet portraitUpsideDown
    }
}
