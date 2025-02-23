package plugin.project.compose.desktop.model.macos

import gradle.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.compose.desktop.application.dsl.InfoPlistSettings

@Serializable
internal data class InfoPlistSettings(
    val extraKeysRawXml: String? = null
) {

    fun applyTo(settings: InfoPlistSettings) {
        settings::extraKeysRawXml trySet extraKeysRawXml
    }
}
