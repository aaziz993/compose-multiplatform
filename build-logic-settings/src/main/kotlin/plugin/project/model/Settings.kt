package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.project.compose.model.ComposeSettings
import plugin.project.gradle.model.GradleSettings
import plugin.project.kotlin.model.KotlinSettings
import plugin.project.web.model.WebSettings

@Serializable
internal data class Settings(
    val kotlin: KotlinSettings = KotlinSettings(),
    val gradle: GradleSettings = GradleSettings(),
    val web: WebSettings = WebSettings(),
    val compose: ComposeSettings = ComposeSettings(),
)
