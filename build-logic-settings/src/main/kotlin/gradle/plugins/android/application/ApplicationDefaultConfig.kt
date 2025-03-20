package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.VariantDimension
import gradle.collection.SerializableAnyMap
import gradle.plugins.android.BuildConfigField
import gradle.plugins.android.DefaultConfigDsl
import gradle.plugins.android.ExternalNativeBuildFlags
import gradle.plugins.android.JavaCompileOptions
import gradle.plugins.android.MissingDimensionStrategy
import gradle.plugins.android.Ndk
import gradle.plugins.android.Optimization
import gradle.plugins.android.ResValue
import gradle.plugins.android.Shaders
import gradle.plugins.android.VectorDrawables
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Specifies defaults for properties that the Android application plugin applies to all build variants.
 *
 * You can override any `defaultConfig` property when
 * [configuring product flavors](https://developer.android.com/studio/build/build-variants.html#product-flavors).
 * See [ApplicationProductFlavor].
 */
internal interface ApplicationDefaultConfig<T : ApplicationDefaultConfig> : ApplicationBaseFlavor<T>, DefaultConfigDsl<T> {

    context(Project)
    override fun applyTo(recipient: T) {
        super<ApplicationBaseFlavor>.applyTo(recipient)
        super<DefaultConfigDsl>.applyTo(recipient)
    }
}

@Serializable
internal data class ApplicationDefaultConfigImpl(
    override val applicationId: String? = null,
    override val versionCode: Int? = null,
    override val versionName: String? = null,
    override val targetSdk: Int? = null,
    override val targetSdkPreview: String? = null,
    override val maxSdk: Int? = null,
    override val name: String = "",
    override val testApplicationId: String? = null,
    override val minSdk: Int? = null,
    override val minSdkPreview: String? = null,
    override val renderscriptTargetApi: Int? = null,
    override val renderscriptSupportModeEnabled: Boolean? = null,
    override val renderscriptSupportModeBlasEnabled: Boolean? = null,
    override val renderscriptNdkModeEnabled: Boolean? = null,
    override val testInstrumentationRunner: String? = null,
    override val testInstrumentationRunnerArguments: Map<String, String>? = null,
    override val setTestInstrumentationRunnerArguments: Map<String, String>? = null,
    override val testHandleProfiling: Boolean? = null,
    override val testFunctionalTest: Boolean? = null,
    override val vectorDrawables: VectorDrawables? = null,
    override val wearAppUnbundled: Boolean? = null,
    override val missingDimensionStrategies: Set<MissingDimensionStrategy>? = null,
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
    override val signingConfig: String? = null,
) : gradle.plugins.android.application.ApplicationDefaultConfig<ApplicationDefaultConfig>
