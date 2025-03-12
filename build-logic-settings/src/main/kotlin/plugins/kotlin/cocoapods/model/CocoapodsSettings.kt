package plugins.kotlin.cocoapods.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.plugins.kotlin.cocoapods.CocoapodsExtension
import gradle.plugins.kotlin.cocoapods.Pod
import gradle.plugins.kmp.nat.FrameworkSettings
import gradle.plugins.project.EnabledSettings
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

@Serializable
internal data class CocoapodsSettings(
    override val version: String? = null,
    override val authors: String? = null,
    override val podfile: String? = null,
    override val needPodspec: Boolean? = null,
    override val name: String? = null,
    override val license: String? = null,
    override val summary: String? = null,
    override val homepage: String? = null,
    override val source: String? = null,
    override val extraSpecAttributes: Map<String, String>? = null,
    override val framework: FrameworkSettings? = null,
    override val ios: CocoapodsExtension.PodspecPlatformSettings? = null,
    override val osx: CocoapodsExtension.PodspecPlatformSettings? = null,
    override val tvos: CocoapodsExtension.PodspecPlatformSettings? = null,
    override val watchos: CocoapodsExtension.PodspecPlatformSettings? = null,
    override val xcodeConfigurationToNativeBuildType: Map<String, NativeBuildType>? = null,
    override val publishDir: String? = null,
    override val specRepos: Set<String>? = null,
    override val pods: List<Pod>? = null,
    override val podDependencies: List<CocoapodsExtension.CocoapodsDependency>? = null,
    override val enabled: Boolean = true
) : CocoapodsExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("cocoapods").id) {
            super.applyTo()
        }
}
