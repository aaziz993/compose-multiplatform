package gradle.plugins.android.defaultconfig

import com.android.build.gradle.internal.dsl.DefaultConfig
import klib.data.type.serialization.serializer.SerializableAnyMap
import gradle.plugins.android.AarMetadata
import gradle.plugins.android.BuildConfigField
import gradle.plugins.android.ExternalNativeBuildFlags
import gradle.plugins.android.MissingDimensionStrategy
import gradle.plugins.android.Ndk
import gradle.plugins.android.Optimization
import gradle.plugins.android.ResValue
import gradle.plugins.android.Shaders
import gradle.plugins.android.VectorDrawables
import gradle.plugins.android.application.ApplicationDefaultConfig
import gradle.plugins.android.compile.JavaCompileOptions
import gradle.plugins.android.library.LibraryDefaultConfig
import gradle.plugins.android.test.TestDefaultConfig
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class DefaultConfig(
    override val applicationId: String? = null,
    override val versionCode: Int? = null,
    override val versionName: String? = null,
    override val targetSdk: Int? = null,
    override val targetSdkPreview: String? = null,
    override val maxSdk: Int? = null,
    override val name: String? = null,
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
    override val setProguardFiles: List<String>? = null,
    override val defaultProguardFiles: List<String>? = null,
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
    override val consumerProguardFiles: List<String>? = null,
    override val setConsumerProguardFiles: List<String>? = null,
    override val aarMetadata: AarMetadata?
) : ApplicationDefaultConfig<DefaultConfig>,
    DynamicFeatureDefaultConfig<DefaultConfig>,
    LibraryDefaultConfig<DefaultConfig>,
    TestDefaultConfig<DefaultConfig> {

    context(Project)
    override fun applyTo(receiver: DefaultConfig) {
        super<ApplicationDefaultConfig>.applyTo(receiver)
        super<DynamicFeatureDefaultConfig>.applyTo(receiver)
        super<LibraryDefaultConfig>.applyTo(receiver)
        super<TestDefaultConfig>.applyTo(receiver)
    }
}
