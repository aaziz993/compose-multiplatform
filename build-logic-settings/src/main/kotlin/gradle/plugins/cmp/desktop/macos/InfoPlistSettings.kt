package gradle.plugins.cmp.desktop.macos

import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.compose.desktop.application.dsl.InfoPlistSettings

@Serializable
internal data class InfoPlistSettings(
    val extraKeysRawXml: String? = null
) {

    fun applyTo(recipient: InfoPlistSettings) {
        recipient::extraKeysRawXml trySet extraKeysRawXml
    }
}
