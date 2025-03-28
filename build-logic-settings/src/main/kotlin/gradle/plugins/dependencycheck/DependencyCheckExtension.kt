package gradle.plugins.dependencycheck

import gradle.accessors.catalog.libs
import gradle.accessors.dependencyCheck
import gradle.accessors.settings
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
internal abstract class DependencyCheckExtension {

    /**
     * Whether the buildEnv should be analyzed.
     */
    abstract val scanBuildEnv: Boolean?

    /**
     * Whether the dependencies should be analyzed.
     */
    abstract val scanDependencies: Boolean?

    /**
     * The configuration extension for proxy settings.
     * Allows programmatic configuration of the slack extension
     * @param config the action to configure the slack extension
     * @return the slack extension
     */
    abstract val slack: SlackExtension?

    /**
     * The configuration extension that defines the location of the NVD CVE data.
     * Allows programmatic configuration of the nvd extension
     * @param config the action to configure the nvd extension
     * @return the nvd extension
     */
    abstract val nvd: NvdExtension?

    /**
     * The configuration extension that configures the hosted suppressions file.
     * Allows programmatic configuration of the hostedSuppressions extension.
     * @param config the action to configure the hostedSuppressions extension
     * @return the hostedSuppressions extension
     */
    abstract val hostedSuppressions: HostedSuppressionsExtension?

    /**
     * Whether the plugin should fail when errors occur.
     */
    abstract val failOnError: Boolean?

    /**
     * The configuration extension for data related configuration options.
     * Allows programmatic configuration of the data extension
     * @param config the action to configure the data extension
     * @return the data extension
     */
    abstract val data: DataExtension?

    /**
     * Set to false if the proxy does not support HEAD requests. The default is true.
     */
    abstract val quickQueryTimestamp: Boolean?

    /**
     * The directory where the reports will be written. Defaults to 'build/reports'.
     */
    abstract val outputDirectory: String?

    /**
     * Configuration for the analyzers.
     * Allows programmatic configuration of the analyzer extension
     * @param config the action to configure the analyzers extension
     * @return the analyzers extension
     */
    abstract val analyzers: AnalyzerExtension?

    /**
     * The path to the suppression file.
     */
    abstract val suppressionFile: String?

    /**
     * The list of paths to suppression files.
     */
    abstract val suppressionFiles: Set<String>?
    abstract val setSuppressionFiles: Set<String>?

    /**
     * The username for downloading the suppression file(s) from HTTP Basic protected locations
     */
    abstract val suppressionFileUser: String?

    /**
     * The password for downloading the suppression file(s) from HTTP Basic protected locations
     */
    abstract val suppressionFilePassword: String?

    /**
     * The token for downloading the suppression file(s) from HTTP Bearer protected locations
     */
    abstract val suppressionFileBearerToken: String?

    /**
     * The path to the hints file.
     */
    abstract val hintsFile: String?

    /**
     * Sets whether auto-updating of the NVD CVE/CPE data is enabled.
     */
    abstract val autoUpdate: Boolean?

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
    abstract val skipTestGroups: Boolean?

    /**
     * The report format to be generated (HTML, XML, CSV, JUNIT, SARIF, ALL). This configuration option has
     * no affect if using this within the Site plugin unless the externalReport is set to true.
     * The default is HTML.
     */
    abstract val format: String?

    /**
     * The list of formats to generate to report (HTML, XML, CSV, JUNIT, SARIF, ALL).
     */
    abstract val formats: List<String>?
    abstract val setFormats: List<String>?

    /**
     * Specifies if the build should be failed if a CVSS score above a specified level is identified. The default is
     * 11 which means since the CVSS scores are 0-10, by default the build will never fail.
     */
    abstract val failBuildOnCVSS: Float?

    /**
     * Specifies the CVSS score that should be considered a failure when generating a JUNIT formatted report. The default
     * is 0.0 which means all identified vulnerabilities would be considered a failure.
     */
    abstract val junitFailOnCVSS: Float?

    /**
     * Specifies that if any unused suppression rule is found, the build will fail.
     */
    abstract val failBuildOnUnusedSuppressionRule: Boolean?

    /**
     * Displays a summary of the findings. Defaults to true.
     */
    abstract val showSummary: Boolean?

    /**
     * Names of the configurations to scan.
     *
     * This is mutually exclusive with the skipConfigurations property.
     */
    abstract val scanConfigurations: List<String>?
    abstract val setScanConfigurations: List<String>?

    /**
     * Names of the configurations to skip when scanning.
     *
     * This is mutually exclusive with the scanConfigurations property.
     */
    abstract val skipConfigurations: List<String>?
    abstract val setSkipConfigurations: List<String>?

    /**
     * Paths of the projects to scan.
     *
     * This is mutually exclusive with the skipProjects property.
     */
    abstract val scanProjects: List<String>?
    abstract val setScanProjects: List<String>?

    /**
     * Paths of the projects to skip when scanning.
     *
     * This is mutually exclusive with the scanProjects property.
     */
    abstract val skipProjects: List<String>?
    abstract val setSkipProjects: List<String>?

    /**
     * Group prefixes of the modules to skip when scanning.
     *
     * The 'project' prefix can be used to skip all internal dependencies from multi-project build.
     */
    abstract val skipGroups: List<String>?
    abstract val setSkipGroups: List<String>?

    /**
     * The artifact types that will be analyzed in the gradle build.
     */
    abstract val analyzedTypes: List<String>?
    abstract val setAnalyzedTypes: List<String>?

    /**
     * whether to skip the execution of dependency-check.
     */
    abstract val skip: Boolean?

    /**
     * A set of files or folders to scan.
     */
    abstract val scanSet: List<String>?
    abstract val setScanSet: List<String>?

    /**
     * Additional CPE to be analyzed.
     * Allows programmatic configuration of additional CPEs to be analyzed
     * @param action the action used to add entries to additional CPEs container.
     */
    abstract val additionalCpes: LinkedHashSet<@Serializable(with = AdditionalCpeKeyTransformingSerializer::class) AdditionalCpe>?

    /**
     * The configuration extension for cache settings.
     * Allows programmatic configuration of the cache extension
     * @param config the action to configure the cache extension
     * @return the cache extension
     */
    abstract val cache: CacheExtension?

    context(Project)
    open fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("dependencycheck").id) {
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
