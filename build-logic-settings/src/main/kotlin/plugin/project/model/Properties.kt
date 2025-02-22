package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.project.jvm.model.JvmSettings
import plugin.project.kotlin.model.Kotlin

@Serializable
internal data class Properties(
    val plugins: Plugins = Plugins(),
    val group: String? = null,
    val description: String? = null,
    val kotlin: Kotlin = Kotlin(),
    val jvm: JvmSettings = JvmSettings(),
    val settings: Settings = Settings(),
    val pluginManagement: PluginManagement? = null,
    val dependencyResolutionManagement: DependencyResolutionManagement? = null,
    val modules: List<String>? = null,
)
