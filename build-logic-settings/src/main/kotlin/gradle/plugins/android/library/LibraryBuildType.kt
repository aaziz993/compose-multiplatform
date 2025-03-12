package gradle.plugins.android.library

import gradle.plugins.android.AarMetadata
import gradle.plugins.android.AndroidTest
import gradle.plugins.android.ApkSigningConfigImpl
import gradle.plugins.android.BuildConfigField
import gradle.plugins.android.BuildType
import gradle.plugins.android.ExternalNativeBuildFlags
import gradle.plugins.android.JavaCompileOptions
import gradle.plugins.android.Ndk
import gradle.plugins.android.Optimization
import gradle.plugins.android.PostProcessing
import gradle.plugins.android.ResValue
import gradle.plugins.android.Shaders
import gradle.plugins.android.VcsInfo
import gradle.collection.SerializableAnyMap
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project

/**
 * Build types define certain properties that Gradle uses when building and packaging your library,
 * and are typically configured for different stages of your development lifecycle.
 *
 * There are two build types defined by default, `debug` and `release`, and you can customize them
 * and create additional build types.
 *
 * The default debug build type enables debug options, while the release build type is not
 * debuggable and can be configured to, for example shrink and obfuscate your library for
 * distribution.
 *
 * See
 * [configuring build types](https://developer.android.com/studio/build#build-config)
 * for more information.
 */
@Serializable
internal data class LibraryBuildType(
    override val name: String,
    override val enableUnitTestCoverage: Boolean? = null,
    override val enableAndroidTestCoverage: Boolean? = null,
    override val isPseudoLocalesEnabled: Boolean? = null,
    override val isJniDebuggable: Boolean? = null,
    override val renderscriptOptimLevel: Int? = null,
    override val ndk: Ndk? = null,
    override val matchingFallbacks: List<String>? = null,
    override val javaCompileOptions: JavaCompileOptions? = null,
    override val shaders: Shaders? = null,
    override val signingConfig: ApkSigningConfigImpl? = null,
    override val proguardFiles: List<String>? = null,
    override val testProguardFiles: List<String>? = null,
    override val consumerProguardFiles: List<String>? = null,
    override val isMinifyEnabled: Boolean? = null,
    override val postprocessing: PostProcessing? = null,
    override val initWith: String? = null,
    override val vcsInfo: VcsInfo? = null,
    override val multiDexEnabled: Boolean? = null,
    override val aarMetadata: AarMetadata? = null,
    override val multiDexKeepProguard: String? = null,
    override val setProguardFiles: List<String>? = null,
    override val manifestPlaceholders: SerializableAnyMap? = null,
    override val externalNativeBuild: ExternalNativeBuildFlags? = null,
    override val buildConfigFields: List<BuildConfigField>? = null,
    override val resValues: List<ResValue>? = null,
    override val optimization: Optimization? = null,
    val androidTest: AndroidTest? = null,
) : BuildType,
    LibraryVariantDimension {

    override val isShrinkResources: Boolean?
        get() = false

    context(Project)
    @Suppress("UnstableApiUsage")
    override fun applyTo(named: Named) {
        super<BuildType>.applyTo(named)

        named as com.android.build.api.dsl.LibraryBuildType

        super<LibraryVariantDimension>.applyTo(named)

        androidTest?.applyTo(named.androidTest)
    }
}

internal object LibraryBuildTypeTransformingSerializer : KeyTransformingSerializer<LibraryBuildType>(
    LibraryBuildType.serializer(),
    "name",
)
