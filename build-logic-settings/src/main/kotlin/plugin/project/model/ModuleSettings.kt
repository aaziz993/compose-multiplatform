package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.project.android.model.AndroidSettings
import plugin.project.apple.model.AppleSettings
import plugin.project.cocoapods.model.CocoapodsSettings
import plugin.project.compose.model.ComposeSettings
import plugin.project.gradle.model.ModuleGradleSettings
import plugin.project.jvm.model.JvmSettings
import plugin.project.kotlin.model.KotlinSettings
import plugin.project.kotlinnative.model.NativeSettings
import plugin.project.web.model.WebSettings

@Serializable
internal data class ModuleSettings(
    val gradle: ModuleGradleSettings = ModuleGradleSettings(),
    val kotlin: KotlinSettings = KotlinSettings(),
    val jvm: JvmSettings = JvmSettings(),
    val android: AndroidSettings = AndroidSettings(),
    val native: NativeSettings = NativeSettings(),
    val apple: AppleSettings = AppleSettings(),
    val cocoapods: CocoapodsSettings = CocoapodsSettings(),
    val web: WebSettings = WebSettings(),
    val compose: ComposeSettings = ComposeSettings(),
)
