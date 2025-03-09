package gradle.model.android.library

import gradle.model.android.AarMetadata
import gradle.model.android.ApkSigningConfig
import gradle.model.android.ApkSigningConfigImpl
import gradle.model.android.BuildConfigField
import gradle.model.android.ExternalNativeBuildFlags
import gradle.model.android.JavaCompileOptions
import gradle.model.android.MissingDimensionStrategy
import gradle.model.android.Ndk
import gradle.model.android.Optimization
import gradle.model.android.ResValue
import gradle.model.android.Shaders
import gradle.model.android.VectorDrawables
import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class LibraryDefaultConfigImpl(
    override val name: String="",
    override val testApplicationId: String? = null,
    override val minSdk: Int? = null,
    override val minSdkPreview: String? = null,
    override val renderscriptTargetApi: Int? = null,
    override val renderscriptSupportModeEnabled: Boolean? = null,
    override val renderscriptSupportModeBlasEnabled: Boolean? = null,
    override val renderscriptNdkModeEnabled: Boolean? = null,
    override val testInstrumentationRunner: String? = null,
    override val testInstrumentationRunnerArguments: Map<String, String>? = null,
    override val testHandleProfiling: Boolean? = null,
    override val testFunctionalTest: Boolean? = null,
    override val vectorDrawables: VectorDrawables? = null,
    override val wearAppUnbundled: Boolean? = null,
    override val missingDimensionStrategies: List<MissingDimensionStrategy>? = null,
    override val initWith: String? = null,
    override val multiDexKeepProguard: String? = null,
    override val ndk: Ndk? = null,
    override val proguardFiles: List<String>? = null,
    override val setProguardFiles: List<String>? = null,
    override val testProguardFiles: List<String>? = null,
    override val manifestPlaceholders: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val javaCompileOptions: JavaCompileOptions? = null,
    override val shaders: Shaders? = null,
    override val externalNativeBuild: ExternalNativeBuildFlags? = null,
    override val buildConfigFields: List<BuildConfigField>? = null,
    override val resValues: List<ResValue>? = null,
    override val optimization: Optimization? = null,
    override val multiDexEnabled: Boolean? = null,
    override val consumerProguardFiles: List<String>? = null,
    override val signingConfig: ApkSigningConfigImpl? = null,
    override val aarMetadata: AarMetadata? = null,
) : LibraryDefaultConfig
