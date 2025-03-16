package gradle.plugins.android.application

import gradle.plugins.android.ApkSigningConfigImpl
import gradle.plugins.android.BuildConfigField
import gradle.plugins.android.ExternalNativeBuildFlags
import gradle.plugins.android.JavaCompileOptions
import gradle.plugins.android.MissingDimensionStrategy
import gradle.plugins.android.Ndk
import gradle.plugins.android.Optimization
import gradle.plugins.android.ResValue
import gradle.plugins.android.Shaders
import gradle.plugins.android.VectorDrawables
import gradle.collection.SerializableAnyMap
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class ApplicationProductFlavorImpl(
    override val applicationId: String? = null,
    override val versionCode: Int? = null,
    override val versionName: String? = null,
    override val targetSdk: Int? = null,
    override val targetSdkPreview: String? = null,
    override val maxSdk: Int? = null,
    override val name: String,
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
    override val defaultProguardFiles: List<String>? = null,
    override val setProguardFiles: List<String>? = null,
    override val setDefaultProguardFiles: List<String>? = null,
    override val testProguardFiles: List<String>? = null,
    override val manifestPlaceholders: SerializableAnyMap? = null,
    override val javaCompileOptions: JavaCompileOptions? = null,
    override val shaders: Shaders? = null,
    override val externalNativeBuild: ExternalNativeBuildFlags? = null,
    override val buildConfigFields: List<BuildConfigField>? = null,
    override val resValues: List<ResValue>? = null,
    override val optimization: Optimization? = null,
    override val applicationIdSuffix: String? = null,
    override val versionNameSuffix: String? = null,
    override val multiDexEnabled: Boolean? = null,
    override val signingConfig: ApkSigningConfigImpl? = null,
    override val dimension: String? = null,
    override val matchingFallbacks: List<String>? = null,
    /** Whether this product flavor should be selected in Studio by default  */
    override val isDefault: Boolean? = null,
) : ApplicationProductFlavor

internal object ApplicationProductFlavorTransformingSerializer : KeyTransformingSerializer<ApplicationProductFlavorImpl>(
    ApplicationProductFlavorImpl.serializer(),
    "name",
)
