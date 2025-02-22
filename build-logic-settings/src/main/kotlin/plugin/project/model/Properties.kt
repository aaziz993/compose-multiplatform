package plugin.project.model

import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import plugin.project.compose.model.ComposeSettings
import plugin.project.java.model.JavaSettings
import plugin.project.kotlin.model.Kotlin

@Serializable
internal data class Properties(
    val application: Boolean = false,
    val plugins: Plugins = Plugins(),
    val group: String? = null,
    val description: String? = null,
    val jvm: JavaSettings = JavaSettings(),
    val kotlin: Kotlin = Kotlin(),
    val compose: ComposeSettings = ComposeSettings(),
    val settings: Settings = Settings(),
    val pluginManagement: PluginManagement? = null,
    val dependencyResolutionManagement: DependencyResolutionManagement? = null,
    val modules: List<String>? = null,
    val gradleEnterpriseAccessKey: String? = null,
    @SerialName("flat-layout")
    val flatLayout: Boolean = false,
)
