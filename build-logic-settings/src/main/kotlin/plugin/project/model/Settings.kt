package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.project.android.model.AndroidSettings
import plugin.project.apple.model.AppleSettings
import plugin.project.kotlin.model.language.nat.KotlinNativeTarget
import plugin.project.web.model.WebSettings

@Serializable
internal data class Settings(
    val android: AndroidSettings = AndroidSettings(),
    val native: KotlinNativeTarget = KotlinNativeTarget(),
    val apple: AppleSettings = AppleSettings(),
    val web: WebSettings = WebSettings(),
)
