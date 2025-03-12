package gradle.plugins.cmp.desktop.model.macos

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.jetbrains.compose.desktop.application.dsl.MacOSSigningSettings

@Serializable
internal data class MacOSSigningSettings(
    val sign: Boolean? = null,
    val identity: String? = null,
    val keychain: String? = null,
    val prefix: String? = null
) {

    fun applyTo(settings: MacOSSigningSettings) {
        settings.sign tryAssign sign
        settings.identity tryAssign identity
        settings.keychain tryAssign keychain
        settings.prefix tryAssign prefix
    }
}
