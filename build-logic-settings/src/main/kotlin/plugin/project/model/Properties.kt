package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.project.android.model.AndroidSettings
import plugin.project.apple.model.AppleSettings
import plugin.project.compose.model.ComposeSettings
import plugin.project.java.application.model.JavaApplication
import plugin.project.java.model.JavaPluginSettings
import plugin.project.kotlin.kmp.model.KotlinMultiplatformSettings
import plugin.project.web.js.karakum.model.KarakumSettings
import plugin.project.web.node.model.EnvSpec
import plugin.project.web.npm.model.NpmSettings
import plugin.project.web.yarn.model.YarnSettings

@Serializable
internal data class Properties(
    val plugins: Plugins = Plugins(),
    val group: String? = null,
    val description: String? = null,
    val jvm: JavaPluginSettings = JavaPluginSettings(),
    val application: JavaApplication? = null,
    val kotlin: KotlinMultiplatformSettings = KotlinMultiplatformSettings(),
    val android: AndroidSettings = AndroidSettings(),
    val apple: AppleSettings = AppleSettings(),
    val nodeJsEnv: EnvSpec = EnvSpec(),
    val yarn: YarnSettings = YarnSettings(),
    val npm: NpmSettings = NpmSettings(),
    val karakum: KarakumSettings = KarakumSettings(),
    val compose: ComposeSettings = ComposeSettings(),
    val pluginManagement: PluginManagement? = null,
    val dependencyResolutionManagement: DependencyResolutionManagement? = null,
    val modules: List<String>? = null,
    val gradleEnterpriseAccessKey: String? = null,
    val layout: Layout = Layout.DEFAULT,
)
