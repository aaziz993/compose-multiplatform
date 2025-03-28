package gradle.plugins.compose.desktop.macos

import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.jetbrains.compose.desktop.application.dsl.MacOSSigningSettings

@Serializable
internal data class MacOSSigningSettings(
    val sign: Boolean? = null,
    val identity: String? = null,
    val keychain: String? = null,
    val prefix: String? = null
) {

    fun applyTo(receiver: MacOSSigningSettings) {
        receiver.sign tryAssign sign
        receiver.identity tryAssign identity
        receiver.keychain tryAssign keychain
        receiver.prefix tryAssign prefix
    }
}
