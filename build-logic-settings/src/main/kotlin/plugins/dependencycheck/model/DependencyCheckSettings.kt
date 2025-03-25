package plugins.dependencycheck.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.buildconfig.BuildConfigExtension
import gradle.plugins.dependencycheck.AdditionalCpe
import gradle.plugins.dependencycheck.CacheExtension
import gradle.plugins.dependencycheck.DataExtension
import gradle.plugins.dependencycheck.DependencyCheckExtension
import gradle.plugins.dependencycheck.HostedSuppressionsExtension
import gradle.plugins.dependencycheck.NvdExtension
import gradle.plugins.dependencycheck.SlackExtension
import gradle.plugins.dependencycheck.analyzer.AnalyzerExtension
import gradle.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class DependencyCheckSettings(
    override val scanBuildEnv: Boolean? = null,
    override val scanDependencies: Boolean? = null,
    override val slack: SlackExtension? = null,
    override val nvd: NvdExtension? = null,
    override val hostedSuppressions: HostedSuppressionsExtension? = null,
    override val failOnError: Boolean? = null,
    override val data: DataExtension? = null,
    override val quickQueryTimestamp: Boolean? = null,
    override val outputDirectory: String? = null,
    override val analyzers: AnalyzerExtension? = null,
    override val suppressionFile: String? = null,
    override val suppressionFiles: Set<String>? = null,
    override val setSuppressionFiles: Set<String>? = null,
    override val suppressionFileUser: String? = null,
    override val suppressionFilePassword: String? = null,
    override val suppressionFileBearerToken: String? = null,
    override val hintsFile: String? = null,
    override val autoUpdate: Boolean? = null,
    override val skipTestGroups: Boolean? = null,
    override val format: String? = null,
    override val formats: List<String>? = null,
    override val setFormats: List<String>? = null,
    override val failBuildOnCVSS: Float? = null,
    override val junitFailOnCVSS: Float? = null,
    override val failBuildOnUnusedSuppressionRule: Boolean? = null,
    override val showSummary: Boolean? = null,
    override val scanConfigurations: List<String>? = null,
    override val setScanConfigurations: List<String>? = null,
    override val skipConfigurations: List<String>? = null,
    override val setSkipConfigurations: List<String>? = null,
    override val scanProjects: List<String>? = null,
    override val setScanProjects: List<String>? = null,
    override val skipProjects: List<String>? = null,
    override val setSkipProjects: List<String>? = null,
    override val skipGroups: List<String>? = null,
    override val setSkipGroups: List<String>? = null,
    override val analyzedTypes: List<String>? = null,
    override val setAnalyzedTypes: List<String>? = null,
    override val skip: Boolean? = null,
    override val scanSet: List<String>? = null,
    override val setScanSet: List<String>? = null,
    override val additionalCpes: Set<AdditionalCpe>? = null,
    override val cache: CacheExtension? = null,
    override val enabled: Boolean = true
) : DependencyCheckExtension(), EnabledSettings {

    context(project: Project)
    override fun applyTo() = project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("dependencycheck").id) {
        super.applyTo()
    }
}
