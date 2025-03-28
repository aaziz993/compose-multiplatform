package gradle.plugins.compose.desktop.macos

import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.compose.desktop.application.dsl.InfoPlistSettings

@Serializable
internal data class InfoPlistSettings(
    val extraKeysRawXml: String? = null
) {

    fun applyTo(receiver: InfoPlistSettings) {
        receiver::extraKeysRawXml trySet extraKeysRawXml
    }
}
