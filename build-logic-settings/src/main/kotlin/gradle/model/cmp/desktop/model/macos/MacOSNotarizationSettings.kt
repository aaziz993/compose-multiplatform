package gradle.model.cmp.desktop.model.macos

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.jetbrains.compose.desktop.application.dsl.MacOSNotarizationSettings

@Serializable
internal data class MacOSNotarizationSettings(
    val appleID: String? = null,
    val password: String? = null,
    val teamID: String? = null,
) {

    fun applyTo(settings: MacOSNotarizationSettings) {
        settings.appleID tryAssign appleID
        settings.password tryAssign password
        settings.teamID tryAssign teamID
    }
}
