package gradle.plugins.android.library

import gradle.collection.SerializableAnyMap
import gradle.plugins.android.AarMetadata
import gradle.plugins.android.BuildConfigField
import gradle.plugins.android.BuildType

import gradle.plugins.android.ExternalNativeBuildFlags
import gradle.plugins.android.Ndk
import gradle.plugins.android.Optimization
import gradle.plugins.android.PostProcessing
import gradle.plugins.android.ResValue
import gradle.plugins.android.Shaders
import gradle.plugins.android.VcsInfo
import gradle.plugins.android.compile.JavaCompileOptions
import gradle.plugins.android.test.AndroidTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
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
@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = LibraryNamedKeyValueTransformingSerializer::class)
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
    override val signingConfig: String? = null,
    override val proguardFiles: List<String>? = null,
    override val defaultProguardFiles: List<String>? = null,
    override val testProguardFiles: List<String>? = null,
    override val consumerProguardFiles: List<String>? = null,
    override val setConsumerProguardFiles: List<String>? = null,
    override val isMinifyEnabled: Boolean? = null,
    override val postprocessing: PostProcessing? = null,
    override val initWith: String? = null,
    override val vcsInfo: VcsInfo? = null,
    override val multiDexEnabled: Boolean? = null,
    override val aarMetadata: AarMetadata? = null,
    override val multiDexKeepProguard: String? = null,
    override val setProguardFiles: List<String>? = null,
    override val setDefaultProguardFiles: List<String>? = null,
    override val manifestPlaceholders: SerializableAnyMap? = null,
    override val externalNativeBuild: ExternalNativeBuildFlags? = null,
    override val buildConfigFields: List<BuildConfigField>? = null,
    override val resValues: List<ResValue>? = null,
    override val optimization: Optimization? = null,
    override val isShrinkResources: Boolean? = null,
    val androidTest: AndroidTest? = null,
) : BuildType<com.android.build.api.dsl.LibraryBuildType>,
    LibraryVariantDimension<com.android.build.api.dsl.LibraryBuildType> {

    context(Project)
    @Suppress("UnstableApiUsage")
    override fun applyTo(receiver: com.android.build.api.dsl.LibraryBuildType) {
        super<BuildType>.applyTo(receiver)

        super<LibraryVariantDimension>.applyTo(receiver)

        androidTest?.applyTo(receiver.androidTest)
    }
}

private object LibraryNamedKeyValueTransformingSerializer : NamedKeyValueTransformingSerializer<LibraryBuildType>(
    LibraryBuildType.generatedSerializer(),
)
