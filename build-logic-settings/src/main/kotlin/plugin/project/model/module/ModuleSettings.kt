package plugin.project.model.module

import kotlinx.serialization.Serializable
import plugin.project.compose.model.ComposeSettings
import plugin.project.gradle.model.ModuleGradleSettings
import plugin.project.kotlin.model.KotlinSettings
import plugin.project.web.model.WebSettings

@Serializable
internal data class ModuleSettings(
    val kotlin: KotlinSettings = KotlinSettings(),
    val gradle: ModuleGradleSettings = ModuleGradleSettings(),
    val web: WebSettings = WebSettings(),
    val compose: ComposeSettings = ComposeSettings(),
)
