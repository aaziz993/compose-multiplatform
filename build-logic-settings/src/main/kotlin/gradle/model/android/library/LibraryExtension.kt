package gradle.model.android.library

import gradle.model.android.AaptOptions
import gradle.model.android.AdbOptions
import gradle.model.android.CompileOptions
import gradle.model.android.CompileSdkAddon
import gradle.model.android.ComposeOptions
import gradle.model.android.DataBinding
import gradle.model.android.ExternalNativeBuild
import gradle.model.android.LibraryRequest
import gradle.model.android.Lint
import gradle.model.android.Packaging
import gradle.model.android.Prefab
import gradle.model.android.PrivacySandbox
import gradle.model.android.SigningConfigImpl
import gradle.model.android.SigningConfigTransformingSerializer
import gradle.model.android.Splits
import gradle.model.android.TestCoverage
import gradle.model.android.TestFixtures
import gradle.model.android.TestOptions
import gradle.model.android.TestedExtension
import gradle.model.android.ViewBinding
import gradle.serialization.serializer.AnySerializer
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
    override val experimentalProperties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
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
