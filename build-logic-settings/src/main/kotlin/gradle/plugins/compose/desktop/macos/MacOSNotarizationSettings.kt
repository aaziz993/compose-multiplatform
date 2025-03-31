package gradle.plugins.compose.desktop.macos

import gradle.api.provider.tryAssign
import kotlinx.serialization.Serializable
import org.jetbrains.compose.desktop.application.dsl.MacOSNotarizationSettings

@Serializable
internal data class MacOSNotarizationSettings(
    val appleID: String? = null,
    val password: String? = null,
    val teamID: String? = null,
) {

    fun applyTo(receiver: MacOSNotarizationSettings) {
        receiver.appleID tryAssign appleID
        receiver.password tryAssign password
        receiver.teamID tryAssign teamID
    }
}
