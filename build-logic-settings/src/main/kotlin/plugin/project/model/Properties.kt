package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.project.compose.model.ComposeSettings
import plugin.project.java.model.JavaPluginSettings
import plugin.project.kotlin.kmp.model.KotlinMultiplatformSettings

@Serializable
internal data class Properties(
    val application: Boolean = false,
    val plugins: Plugins = Plugins(),
    val group: String? = null,
    val description: String? = null,
    val jvm: JavaPluginSettings = JavaPluginSettings(),
    val kotlin: KotlinMultiplatformSettings = KotlinMultiplatformSettings(),
    val compose: ComposeSettings = ComposeSettings(),
    val settings: Settings = Settings(),
    val pluginManagement: PluginManagement? = null,
    val dependencyResolutionManagement: DependencyResolutionManagement? = null,
    val modules: List<String>? = null,
    val gradleEnterpriseAccessKey: String? = null,
    val layout: Layout = Layout.DEFAULT,
)
