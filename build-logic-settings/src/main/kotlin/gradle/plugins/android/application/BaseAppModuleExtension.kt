package gradle.plugins.android.application

import gradle.collection.SerializableAnyMap
import gradle.plugins.android.AaptOptions
import gradle.plugins.android.AdbOptions
import gradle.plugins.android.ComposeOptions
import gradle.plugins.android.DependenciesInfo
import gradle.plugins.android.ExternalNativeBuild
import gradle.plugins.android.LibraryRequest
import gradle.plugins.android.Lint
import gradle.plugins.android.Packaging
import gradle.plugins.android.PrivacySandbox
import gradle.plugins.android.application.bundle.Bundle
import gradle.plugins.android.compile.CompileOptions
import gradle.plugins.android.compile.CompileSdkAddon
import gradle.plugins.android.defaultconfig.DefaultConfig
import gradle.plugins.android.features.DataBinding
import gradle.plugins.android.features.ViewBinding
import gradle.plugins.android.flavor.ProductFlavor
import gradle.plugins.android.signing.SigningConfigImpl
import gradle.plugins.android.sourceset.AndroidSourceSet
import gradle.plugins.android.split.Splits
import gradle.plugins.android.test.TestCoverage
import gradle.plugins.android.test.TestFixtures
import gradle.plugins.android.test.TestOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/** The `android` extension for base feature module (application plugin).  */
@Serializable
@SerialName("baseApp")
internal data class BaseAppModuleExtension(
    override val composeOptions: ComposeOptions? = null,
    override val sourceSets: LinkedHashSet<AndroidSourceSet>? = null,
    override val dataBinding: DataBinding? = null,
    override val viewBinding: ViewBinding? = null,
    override val defaultPublishConfig: String? = null,
    override val disableWrite: Boolean? = null,
    override val compileSdkVersion: Int? = null,
    override val buildToolsVersion: String? = null,
    override val flavorDimensions: LinkedHashSet<String>? = null,
    override val setFlavorDimensions: LinkedHashSet<String>? = null,
    override val aaptOptions: AaptOptions? = null,
    override val externalNativeBuild: ExternalNativeBuild? = null,
    override val testOptions: TestOptions? = null,
    override val compileOptions: CompileOptions? = null,
    override val packaging: Packaging? = null,
    override val adbOptions: AdbOptions? = null,
    override val splits: Splits? = null,
    override val generatePureSplits: Boolean? = null,
    override val resourcePrefix: String? = null,
    override val ndkVersion: String? = null,
    override val ndkPath: String? = null,
    override val libraryRequests: Set<LibraryRequest>? = null,
    override val setLibraryRequests: Set<LibraryRequest>? = null,
    override val buildTypes: LinkedHashSet<ApplicationBuildType>? = null,
    override val defaultConfig: DefaultConfig? = null,
    override val productFlavors: LinkedHashSet<ProductFlavor>? = null,
    override val signingConfigs: LinkedHashSet<SigningConfigImpl>? = null,
    override val buildFeatures: ApplicationBuildFeatures? = null,
    override val namespace: String? = null,
    override val testBuildType: String? = null,
    override val testNamespace: String? = null,
    override val testFixtures: TestFixtures? = null,
    override val dependenciesInfo: DependenciesInfo? = null,
    override val bundle: Bundle? = null,
    override val dynamicFeatures: Set<String>? = null,
    override val setDynamicFeatures: Set<String>? = null,
    override val assetPacks: Set<String>? = null,
    override val setAssetPacks: Set<String>? = null,
    override val publishing: ApplicationPublishing? = null,
    override val privacySandbox: PrivacySandbox? = null,
    override val androidResources: ApplicationAndroidResources? = null,
    override val installation: ApplicationInstallation? = null,
    override val testCoverage: TestCoverage? = null,
    override val lint: Lint? = null,
    override val useLibraries: Set<LibraryRequest>? = null,
    override val compileSdk: Int? = null,
    override val compileSdkExtension: Int? = null,
    override val compileSdkPreview: String? = null,
    override val compileSdkAddon: CompileSdkAddon? = null,
    override val experimentalProperties: SerializableAnyMap? = null,
) : AppExtension(), InternalApplicationExtension {

    context(Project)
    override fun applyTo() {
        super<AppExtension>.applyTo()
        super<InternalApplicationExtension>.applyTo()
    }
}
