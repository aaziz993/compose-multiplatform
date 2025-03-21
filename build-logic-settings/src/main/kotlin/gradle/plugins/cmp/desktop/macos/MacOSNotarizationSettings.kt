package gradle.plugins.cmp.desktop.macos

import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.jetbrains.compose.desktop.application.dsl.MacOSNotarizationSettings

@Serializable
internal data class MacOSNotarizationSettings(
    val appleID: String? = null,
    val password: String? = null,
    val teamID: String? = null,
) {

    fun applyTo(recipient: MacOSNotarizationSettings) {
        recipient.appleID tryAssign appleID
        recipient.password tryAssign password
        recipient.teamID tryAssign teamID
    }
}
