package gradle.plugins.dependencycheck.analyzer

import gradle.reflect.trySet
import kotlinx.serialization.Serializable
import org.owasp.dependencycheck.gradle.extension.AnalyzerExtension

/**
 * The analyzer configuration extension. Any value not configured will use the dependency-check-core defaults.
 */
@Serializable
internal data class AnalyzerExtension(
    /**
     * Sets whether the experimental analyzers will be used.
     */
    val experimentalEnabled: Boolean? = null,
    /**
     * Sets whether the Archive Analyzer will be used.
     */
    val archiveEnabled: Boolean? = null,
    /**
     * A comma-separated list of additional file extensions to be treated like a ZIP file, the contents will be extracted and analyzed.
     */
    val zipExtensions: String? = null,
    /**
     * Sets whether Jar Analyzer will be used.
     */
    val jarEnabled: Boolean? = null,
    /**
     * Sets whether Central Analyzer will be used. If this analyzer is being disabled there is a good chance you also want to disable the Nexus Analyzer (see below).
     */
    val centralEnabled: Boolean? = null,
    /**
     * Sets whether Nexus Analyzer will be used. This analyzer is superceded by the Central Analyzer; however, you can configure this to run against a Nexus Pro installation.
     */
    val nexusEnabled: Boolean? = null,
    /**
     * Defines the Nexus Server's web service end point (example http://domain.enterprise/service/local/). If not set the Nexus Analyzer will be disabled.
     */
    val nexusUrl: String? = null,
    /**
     * whether the defined proxy should be used when connecting to Nexus.
     */
    val nexusUsesProxy: Boolean? = null,
    /**
     * Sets whether the .NET Nuget Nuspec Analyzer will be used.
     */
    val nuspecEnabled: Boolean? = null,
    /**
     * Sets whether the .NET Assembly Analyzer should be used.
     */
    val assemblyEnabled: Boolean? = null,
    /**
     * Sets whether the MS Build Analyzer should be used.
     */
    val msbuildEnabled: Boolean? = null,
    /**
     * The path to dotnet core - used to analyze dot net assemblies.
     */
    val pathToDotnet: String? = null,
    /**
     * Sets whether the Golang Dependency analyzer is enabled. Default is true.
     */
    val golangDepEnabled: Boolean? = null,
    /**
     * Sets whether Golang Module Analyzer is enabled; this requires `go` to be
     * installed. Default is true.
     */
    val golangModEnabled: Boolean? = null,
    /**
     * The path to `go` - used to analyze go modules via `go mod`.
     */
    val pathToGo: String? = null,
    /**
     * Sets whether the cocoapods analyzer is enabled.
     */
    val cocoapodsEnabled: Boolean? = null,
    /**
     * Sets whether the swift package manager analyzer is enabled.
     */
    val swiftEnabled: Boolean? = null,
    /**
     * Sets whether the swift package manager analyzer is enabled.
     */
    val dartEnabled: Boolean? = null,
    /**
     * Sets whether the swift package resolved analyzer is enabled.
     */
    val swiftPackageResolvedEnabled: Boolean? = null,
    /**
     * Sets whether the Ruby Bundle Audit analyzer is enabled; requires running bundle audit.
     */
    val bundleAuditEnabled: Boolean? = null,
    /**
     * The path to Ruby's bundle audit.
     */
    val pathToBundleAudit: String? = null,
    /**
     * Sets whether the Python Distribution Analyzer will be used.
     */
    val pyDistributionEnabled: Boolean? = null,
    /**
     * Sets whether the Python Package Analyzer will be used.
     */
    val pyPackageEnabled: Boolean? = null,
    /**
     * Sets whether the Ruby Gemspec Analyzer will be used.
     */
    val rubygemsEnabled: Boolean? = null,
    /**
     * Sets whether the openssl Analyzer should be used.
     */
    val opensslEnabled: Boolean? = null,
    /**
     * Sets whether the CMake Analyzer should be used.
     */
    val cmakeEnabled: Boolean? = null,
    /**
     * Sets whether the autoconf Analyzer should be used.
     */
    val autoconfEnabled: Boolean? = null,
    /**
     * Sets whether the PHP Composer Lock File Analyzer should be used.
     */
    val composerEnabled: Boolean? = null,
    /**
     * Sets whether the PHP Composer Lock File Analyzer should skip packages-dev dependencies.
     */
    val composerSkipDev: Boolean? = null,
    /**
     * Sets whether the Perl CPAN File Analyzer should be used.
     */
    val cpanEnabled: Boolean? = null,
    /**
     * Sets whether the Nuget packages.config Configuration Analyzer should be used.
     */
    val nugetconfEnabled: Boolean? = null,
    /**
     * The configuration extension for known exploited vulnerabilities settings.
     * Allows programmatic configuration of the KEV extension
     * @param config the action to configure the KEV extension
     * @return the KEV extension
     */
    val kev: KEVExtension? = null,
    /**
     * The configuration extension for retirejs settings.
     * Allows programmatic configuration of the retirejs extension
     * @param config the action to configure the retirejs extension
     * @return the retirejs extension
     */
    val retirejs: RetireJSExtension? = null,
    /**
     * The configuration extension for the node audit settings.
     * Allows programmatic configuration of the nodeAudit extension
     * @param config the action to configure the ossIndex extension
     * @return the ossIndex extension
     */
    val nodeAudit: NodeAuditExtension? = null,
    /**
     * The configuration extension for the node package settings.
     * Allows programmatic configuration of the node package extension
     * @param config the action to configure the node extension
     * @return the node extension
     */
    val nodePackage: NodePackageExtension? = null,
    /**
     * The configuration extension for artifactory settings.
     * Allows programmatic configuration of the artifactory extension
     * @param config the action to configure the artifactory extension
     * @return the artifactory extension
     */
    val artifactory: ArtifactoryExtension? = null,
    /**
     * The configuration extension for artifactory settings.
     * Allows programmatic configuration of the ossIndex extension
     * @param config the action to configure the ossIndex extension
     * @return the ossIndex extension
     */
    val ossIndex: OssIndexExtension? = null,
) {

    fun applyTo(receiver: AnalyzerExtension) {
        receiver::setExperimentalEnabled trySet experimentalEnabled
        receiver::setArchiveEnabled trySet archiveEnabled
        receiver::setZipExtensions trySet zipExtensions
        receiver::setJarEnabled trySet jarEnabled
        receiver::setCentralEnabled trySet centralEnabled
        receiver::setNexusEnabled trySet nexusEnabled
        receiver::setNexusUrl trySet nexusUrl
        receiver::setNexusUsesProxy trySet nexusUsesProxy
        receiver::setNuspecEnabled trySet nuspecEnabled
        receiver::setAssemblyEnabled trySet assemblyEnabled
        receiver::setMsbuildEnabled trySet msbuildEnabled
        receiver::setPathToDotnet trySet pathToDotnet
        receiver::setGolangDepEnabled trySet golangDepEnabled
        receiver::setGolangModEnabled trySet golangModEnabled
        receiver::setPathToGo trySet pathToGo
        receiver::setCocoapodsEnabled trySet cocoapodsEnabled
        receiver::setSwiftEnabled trySet swiftEnabled
        receiver::setDartEnabled trySet dartEnabled
        receiver::setSwiftPackageResolvedEnabled trySet swiftPackageResolvedEnabled
        receiver::setBundleAuditEnabled trySet bundleAuditEnabled
        receiver::setPathToBundleAudit trySet pathToBundleAudit
        receiver::setPyDistributionEnabled trySet pyDistributionEnabled
        receiver::setPyPackageEnabled trySet pyPackageEnabled
        receiver::setRubygemsEnabled trySet rubygemsEnabled
        receiver::setOpensslEnabled trySet opensslEnabled
        receiver::setCmakeEnabled trySet cmakeEnabled
        receiver::setAutoconfEnabled trySet autoconfEnabled
        receiver::setComposerEnabled trySet composerEnabled
        receiver::setComposerSkipDev trySet composerSkipDev
        receiver::setCpanEnabled trySet cpanEnabled
        receiver::setNugetconfEnabled trySet nugetconfEnabled
        kev?.applyTo(receiver.kev)
        retirejs?.applyTo(receiver.retirejs)
        nodeAudit?.applyTo(receiver.nodeAudit)
        nodePackage?.applyTo(receiver.nodePackage)
        artifactory?.applyTo(receiver.artifactory)
        ossIndex?.applyTo(receiver.ossIndex)
    }
}
