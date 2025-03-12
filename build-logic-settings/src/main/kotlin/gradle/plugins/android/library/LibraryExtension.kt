package gradle.plugins.android.library

import gradle.plugins.android.AaptOptions
import gradle.plugins.android.AdbOptions
import gradle.plugins.android.CompileOptions
import gradle.plugins.android.CompileSdkAddon
import gradle.plugins.android.ComposeOptions
import gradle.plugins.android.DataBinding
import gradle.plugins.android.ExternalNativeBuild
import gradle.plugins.android.LibraryRequest
import gradle.plugins.android.Lint
import gradle.plugins.android.Packaging
import gradle.plugins.android.Prefab
import gradle.plugins.android.PrivacySandbox
import gradle.plugins.android.SigningConfigImpl
import gradle.plugins.android.SigningConfigTransformingSerializer
import gradle.plugins.android.Splits
import gradle.plugins.android.TestCoverage
import gradle.plugins.android.TestFixtures
import gradle.plugins.android.TestOptions
import gradle.plugins.android.TestedExtension
import gradle.plugins.android.ViewBinding
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * The {@code android} extension for {@code com.android.library} projects.
 *
 * <p>Apply this plugin to your project to <a
 * href="https://developer.android.com/studio/projects/android-library.html">create an Android
 * library</a>.
 */
@Serializable
internal data class LibraryExtension(
    override val androidResources: LibraryAndroidResources? = null,
    override val installation: LibraryInstallation? = null,
    override val buildFeatures: LibraryBuildFeatures? = null,
    override val buildTypes: List<@Serializable(with = LibraryBuildTypeTransformingSerializer::class) LibraryBuildType>? = null,
    override val testCoverage: TestCoverage? = null,
    override val lint: Lint? = null,
    override val productFlavors: List<@Serializable(with = LibraryProductFlavorTransformingSerializer::class) LibraryProductFlavorImpl>? = null,
    override val defaultConfig: LibraryDefaultConfigImpl? = null,
    override val useLibraries: List<LibraryRequest>? = null,
    override val compileSdk: Int? = null,
    override val compileSdkExtension: Int? = null,
    override val compileSdkPreview: String? = null,
    override val compileSdkAddon: CompileSdkAddon? = null,
    override val experimentalProperties: SerializableAnyMap? = null,
    override val aidlPackagedList: List<String>? = null,
    override val prefab: List<Prefab>? = null,
    override val publishing: LibraryPublishing? = null,
    override val privacySandbox: PrivacySandbox? = null,
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
    override val signingConfigs: List<@Serializable(with = SigningConfigTransformingSerializer::class) SigningConfigImpl>? = null,
    override val namespace: String? = null,
    override val testBuildType: String? = null,
    override val testNamespace: String? = null,
    override val testFixtures: TestFixtures? = null
) : TestedExtension(), InternalLibraryExtension {

    context(Project)
    override fun applyTo() {
        super<TestedExtension>.applyTo()
        super<InternalLibraryExtension>.applyTo()
    }
}
