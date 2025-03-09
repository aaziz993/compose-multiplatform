package gradle.model.kotlin.kmp.jvm.android.application

import gradle.model.kotlin.kmp.jvm.android.AaptOptions
import gradle.model.kotlin.kmp.jvm.android.AdbOptions
import gradle.model.kotlin.kmp.jvm.android.Bundle
import gradle.model.kotlin.kmp.jvm.android.CompileOptions
import gradle.model.kotlin.kmp.jvm.android.CompileSdkAddon
import gradle.model.kotlin.kmp.jvm.android.ComposeOptions
import gradle.model.kotlin.kmp.jvm.android.DataBinding
import gradle.model.kotlin.kmp.jvm.android.DependenciesInfo
import gradle.model.kotlin.kmp.jvm.android.ExternalNativeBuild
import gradle.model.kotlin.kmp.jvm.android.LibraryRequest
import gradle.model.kotlin.kmp.jvm.android.Lint
import gradle.model.kotlin.kmp.jvm.android.Packaging
import gradle.model.kotlin.kmp.jvm.android.PrivacySandbox
import gradle.model.kotlin.kmp.jvm.android.SigningConfigImpl
import gradle.model.kotlin.kmp.jvm.android.Splits
import gradle.model.kotlin.kmp.jvm.android.TestCoverage
import gradle.model.kotlin.kmp.jvm.android.TestFixtures
import gradle.model.kotlin.kmp.jvm.android.TestOptions
import gradle.model.kotlin.kmp.jvm.android.ViewBinding
import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/** The `android` extension for base feature module (application plugin).  */
@Serializable
internal data class BaseAppModuleExtension(
    override val composeOptions: ComposeOptions? = null,
    override val dataBinding: DataBinding? = null,
    override val viewBinding: ViewBinding? = null,
    override val defaultPublishConfig: String? = null,
    override val disableWrite: Boolean? = null,
    override val compileSdkVersion: Int? = null,
    override val buildToolsVersion: String? = null,
    override val flavorDimensions: List<String>? = null,
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
    override val libraryRequests: List<LibraryRequest>? = null,
    override val buildTypes: List<@Serializable(with = ApplicationBuildTypeTransformingSerializer::class) ApplicationBuildType>? = null,
    override val defaultConfig: ApplicationDefaultConfigImpl? = null,
    override val productFlavors: List<@Serializable(with = ApplicationProductFlavorTransformingSerializer::class) ApplicationProductFlavorImpl>? = null,
    override val signingConfigs: List<SigningConfigImpl>? = null,
    override val buildFeatures: ApplicationBuildFeatures? = null,
    override val namespace: String? = null,
    override val testBuildType: String? = null,
    override val testNamespace: String? = null,
    override val testFixtures: TestFixtures? = null,
    override val dependenciesInfo: DependenciesInfo? = null,
    override val bundle: Bundle? = null,
    override val dynamicFeatures: Set<String>? = null,
    override val assetPacks: Set<String>? = null,
    override val publishing: ApplicationPublishing? = null,
    override val privacySandbox: PrivacySandbox? = null,
    override val androidResources: ApplicationAndroidResources? = null,
    override val installation: ApplicationInstallation? = null,
    override val testCoverage: TestCoverage? = null,
    override val lint: Lint? = null,
    override val useLibraries: List<LibraryRequest>? = null,
    override val compileSdk: Int? = null,
    override val compileSdkExtension: Int? = null,
    override val compileSdkPreview: String? = null,
    override val compileSdkAddon: CompileSdkAddon? = null,
    override val experimentalProperties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
) : AppExtension(), InternalApplicationExtension {

    context(Project)
    override fun applyTo() {
        super<AppExtension>.applyTo()
        super<InternalApplicationExtension>.applyTo()
    }
}
