package gradle.plugins.dependencycheck

import gradle.accessors.dependencyCheck
import gradle.api.applyTo
import gradle.api.tryAddAll
import gradle.api.trySet
import gradle.plugins.dependencycheck.analyzer.AnalyzerExtension
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/*
 * Configuration extension for the dependencyCheck plugin.
 *
 * @author Wei Ma
 * @author Jeremy Long
 */
@Serializable
internal data class DependencyCheckExtension(
    /**
     * Whether the buildEnv should be analyzed.
     */
    val scanBuildEnv: Boolean? = null,
    /**
     * Whether the dependencies should be analyzed.
     */
    val scanDependencies: Boolean? = null,
    /**
     * The configuration extension for proxy settings.
     * Allows programmatic configuration of the slack extension
     * @param config the action to configure the slack extension
     * @return the slack extension
     */
    val slack: SlackExtension? = null,
    /**
     * The configuration extension that defines the location of the NVD CVE data.
     * Allows programmatic configuration of the nvd extension
     * @param config the action to configure the nvd extension
     * @return the nvd extension
     */
    val nvd: NvdExtension? = null,
    /**
     * The configuration extension that configures the hosted suppressions file.
     * Allows programmatic configuration of the hostedSuppressions extension.
     * @param config the action to configure the hostedSuppressions extension
     * @return the hostedSuppressions extension
     */
    val hostedSuppressions: HostedSuppressionsExtension? = null,
    /**
     * Whether the plugin should fail when errors occur.
     */
    val failOnError: Boolean? = null,
    /**
     * The configuration extension for data related configuration options.
     * Allows programmatic configuration of the data extension
     * @param config the action to configure the data extension
     * @return the data extension
     */
    val data: DataExtension? = null,
    /**
     * Set to false if the proxy does not support HEAD requests. The default is true.
     */
    val quickQueryTimestamp: Boolean? = null,
    /**
     * The directory where the reports will be written. Defaults to 'build/reports'.
     */
    val outputDirectory: String? = null,
    /**
     * Configuration for the analyzers.
     * Allows programmatic configuration of the analyzer extension
     * @param config the action to configure the analyzers extension
     * @return the analyzers extension
     */
    val analyzers: AnalyzerExtension? = null,
    /**
     * The path to the suppression file.
     */
    val suppressionFile: String? = null,
    /**
     * The list of paths to suppression files.
     */
    val suppressionFiles: Set<String>? = null,
    val setSuppressionFiles: Set<String>? = null,
    /**
     * The username for downloading the suppression file(s) from HTTP Basic protected locations
     */
    val suppressionFileUser: String? = null,
    /**
     * The password for downloading the suppression file(s) from HTTP Basic protected locations
     */
    val suppressionFilePassword: String? = null,
    /**
     * The token for downloading the suppression file(s) from HTTP Bearer protected locations
     */
    val suppressionFileBearerToken: String? = null,
    /**
     * The path to the hints file.
     */
    val hintsFile: String? = null,
    /**
     * Sets whether auto-updating of the NVD CVE/CPE data is enabled.
     */
    val autoUpdate: Boolean? = null,
    //The following properties are not used via the settings object, instead
    // they are directly used by the check task.
    /**
     * When set to true configurations that are considered a test configuration will not be included in the analysis.
     * A configuration is considered a test configuration if and only if any of the following conditions holds:
     *
     *  * the name of the configuration or any of its parent configurations equals 'testCompile'
     *  * the name of the configuration or any of its parent configurations equals 'testImplementation'
     *  * the name of the configuration or any of its parent configurations equals 'androidTestCompile'
     *  * the configuration name starts with 'test'
     *  * the configuration name starts with 'androidTest'
     *
     * The default value is true.
     */
    val skipTestGroups: Boolean? = null,
    /**
     * The report format to be generated (HTML, XML, CSV, JUNIT, SARIF, ALL). This configuration option has
     * no affect if using this within the Site plugin unless the externalReport is set to true.
     * The default is HTML.
     */
    val format: String? = null,
    /**
     * The list of formats to generate to report (HTML, XML, CSV, JUNIT, SARIF, ALL).
     */
    val formats: List<String>? = null,
    val setFormats: List<String>? = null,
    /**
     * Specifies if the build should be failed if a CVSS score above a specified level is identified. The default is
     * 11 which means since the CVSS scores are 0-10, by default the build will never fail.
     */
    val failBuildOnCVSS: Float? = null,
    /**
     * Specifies the CVSS score that should be considered a failure when generating a JUNIT formatted report. The default
     * is 0.0 which means all identified vulnerabilities would be considered a failure.
     */
    val junitFailOnCVSS: Float? = null,
    /**
     * Specifies that if any unused suppression rule is found, the build will fail.
     */
    val failBuildOnUnusedSuppressionRule: Boolean? = null,
    /**
     * Displays a summary of the findings. Defaults to true.
     */
    val showSummary: Boolean? = null,
    /**
     * Names of the configurations to scan.
     *
     * This is mutually exclusive with the skipConfigurations property.
     */
    val scanConfigurations: List<String>? = null,
    val setScanConfigurations: List<String>? = null,
    /**
     * Names of the configurations to skip when scanning.
     *
     * This is mutually exclusive with the scanConfigurations property.
     */
    val skipConfigurations: List<String>? = null,
    val setSkipConfigurations: List<String>? = null,
    /**
     * Paths of the projects to scan.
     *
     * This is mutually exclusive with the skipProjects property.
     */
    val scanProjects: List<String>? = null,
    val setScanProjects: List<String>? = null,
    /**
     * Paths of the projects to skip when scanning.
     *
     * This is mutually exclusive with the scanProjects property.
     */
    val skipProjects: List<String>? = null,
    val setSkipProjects: List<String>? = null,
    /**
     * Group prefixes of the modules to skip when scanning.
     *
     * The 'project' prefix can be used to skip all internal dependencies from multi-project build.
     */
    val skipGroups: List<String>? = null,
    val setSkipGroups: List<String>? = null,
    /**
     * The artifact types that will be analyzed in the gradle build.
     */
    val analyzedTypes: List<String>? = null,
    val setAnalyzedTypes: List<String>? = null,
    /**
     * whether to skip the execution of dependency-check.
     */
    val skip: Boolean? = null,
    /**
     * A set of files or folders to scan.
     */
    val scanSet: List<String>? = null,
    val setScanSet: List<String>? = null,
    /**
     * Additional CPE to be analyzed.
     * Allows programmatic configuration of additional CPEs to be analyzed
     * @param action the action used to add entries to additional CPEs container.
     */
    val additionalCpes: LinkedHashSet<AdditionalCpe>? = null,
    /**
     * The configuration extension for cache settings.
     * Allows programmatic configuration of the cache extension
     * @param config the action to configure the cache extension
     * @return the cache extension
     */
    val cache: CacheExtension?
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("org.owasp.dependencycheck") {
            project.dependencyCheck::setScanBuildEnv trySet scanBuildEnv
            project.dependencyCheck::setScanDependencies trySet scanDependencies
            slack?.applyTo(project.dependencyCheck.slack)
            nvd?.applyTo(project.dependencyCheck.nvd)
            hostedSuppressions?.applyTo(project.dependencyCheck.hostedSuppressions)
            project.dependencyCheck::setFailOnError trySet failOnError
            data?.applyTo(project.dependencyCheck.data)
            project.dependencyCheck::setQuickQueryTimestamp trySet quickQueryTimestamp
            project.dependencyCheck::setOutputDirectory trySet outputDirectory
            analyzers?.applyTo(project.dependencyCheck.analyzers)
            project.dependencyCheck::setSuppressionFile trySet suppressionFile
            project.dependencyCheck.suppressionFiles tryAddAll suppressionFiles
            setSuppressionFiles?.let(project.dependencyCheck::setSuppressionFiles)
            project.dependencyCheck::setSuppressionFileUser trySet suppressionFileUser
            project.dependencyCheck::setSuppressionFilePassword trySet suppressionFilePassword
            project.dependencyCheck::setSuppressionFileBearerToken trySet suppressionFileBearerToken
            project.dependencyCheck::setHintsFile trySet hintsFile
            project.dependencyCheck::setAutoUpdate trySet autoUpdate
            project.dependencyCheck::setSkipTestGroups trySet skipTestGroups
            project.dependencyCheck::setFormat trySet format
            project.dependencyCheck.formats tryAddAll formats
            project.dependencyCheck::setFormats trySet setFormats
            project.dependencyCheck::setFailBuildOnCVSS trySet failBuildOnCVSS
            project.dependencyCheck::setJunitFailOnCVSS trySet junitFailOnCVSS
            project.dependencyCheck::setFailBuildOnUnusedSuppressionRule trySet failBuildOnUnusedSuppressionRule
            project.dependencyCheck::setShowSummary trySet showSummary
            project.dependencyCheck.scanConfigurations tryAddAll scanConfigurations
            project.dependencyCheck::setScanConfigurations trySet setScanConfigurations
            project.dependencyCheck.skipConfigurations tryAddAll skipConfigurations
            project.dependencyCheck::setSkipConfigurations trySet setSkipConfigurations
            project.dependencyCheck.scanProjects tryAddAll scanProjects
            project.dependencyCheck::setScanProjects trySet setScanProjects
            project.dependencyCheck.skipProjects tryAddAll skipProjects
            project.dependencyCheck::setSkipProjects trySet setSkipProjects
            project.dependencyCheck.skipGroups tryAddAll skipGroups
            project.dependencyCheck::setSkipGroups trySet setSkipGroups
            project.dependencyCheck.analyzedTypes tryAddAll analyzedTypes
            project.dependencyCheck::setAnalyzedTypes trySet setAnalyzedTypes
            project.dependencyCheck::setSkip trySet skip
            project.dependencyCheck.scanSet tryAddAll scanSet?.map(project::file)
            project.dependencyCheck::setScanSet trySet setScanSet?.map(project::file)

            additionalCpes?.forEach { additionalCpe ->
                additionalCpe.applyTo(project.dependencyCheck.additionalCpes)
            }

            cache?.applyTo(project.dependencyCheck.cache)
        }
}
