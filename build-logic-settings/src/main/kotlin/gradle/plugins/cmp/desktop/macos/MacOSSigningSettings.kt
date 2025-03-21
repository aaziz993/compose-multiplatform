package gradle.plugins.cmp.desktop.macos

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

    fun applyTo(recipient: MacOSSigningSettings) {
        recipient.sign tryAssign sign
        recipient.identity tryAssign identity
        recipient.keychain tryAssign keychain
        recipient.prefix tryAssign prefix
    }
}
