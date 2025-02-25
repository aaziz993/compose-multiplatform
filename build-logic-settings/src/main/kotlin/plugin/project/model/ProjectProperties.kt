package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.model.dependency.ProjectDependency
import plugin.project.android.model.AndroidSettings
import plugin.project.apple.model.AppleSettings
import plugin.project.compose.model.ComposeSettings
import plugin.project.java.model.JavaPluginExtension
import plugin.project.java.model.application.JavaApplication
import plugin.project.kotlin.kmp.model.KotlinMultiplatformSettings
import plugin.project.web.js.karakum.model.KarakumSettings
import plugin.project.web.node.model.NodeJsEnvSpec
import plugin.project.web.npm.model.NpmExtension
import plugin.project.web.yarn.model.YarnRootExtension

@Serializable
internal data class ProjectProperties(
    val type: ProjectType = ProjectType.LIB,
    val layout: ProjectLayout = ProjectLayout.DEFAULT,
    val group: String? = null,
    val description: String? = null,
    val plugins: Plugins = Plugins(),
    val jvm: JavaPluginExtension? = null,
    val application: JavaApplication? = null,
    val kotlin: KotlinMultiplatformSettings = KotlinMultiplatformSettings(),
    val android: AndroidSettings = AndroidSettings(),
    val apple: AppleSettings = AppleSettings(),
    val nodeJsEnv: NodeJsEnvSpec = NodeJsEnvSpec(),
    val yarn: YarnRootExtension = YarnRootExtension(),
    val npm: NpmExtension = NpmExtension(),
    val karakum: KarakumSettings = KarakumSettings(),
    val compose: ComposeSettings = ComposeSettings(),
    val pluginManagement: PluginManagement? = null,
    val dependencyResolutionManagement: DependencyResolutionManagement? = null,
    val dependencies: List<ProjectDependency>? = null,
    val includes: List<String>? = null,
    val projects: List<ProjectDescriptor>? = null,
    val gradleEnterpriseAccessKey: String? = null,
)
