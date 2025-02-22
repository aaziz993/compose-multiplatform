package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.project.android.model.AndroidSettings
import plugin.project.apple.model.AppleSettings
import plugin.project.cocoapods.model.CocoapodsSettings
import plugin.project.compose.model.ComposeSettings
import plugin.project.kotlinnative.model.NativeSettings
import plugin.project.web.model.WebSettings

@Serializable
internal data class Settings(
    val android: AndroidSettings = AndroidSettings(),
    val native: NativeSettings = NativeSettings(),
    val apple: AppleSettings = AppleSettings(),
    val web: WebSettings = WebSettings(),
)
