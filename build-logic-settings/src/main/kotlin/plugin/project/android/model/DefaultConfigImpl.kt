package plugin.project.android.model

import com.android.build.api.dsl.VariantDimension
import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class DefaultConfigImpl(
    override var applicationId: String? = null,
    override var versionCode: Int? = null,
    override var versionName: String? = null,
    override var targetSdk: Int? = null,
    override var targetSdkPreview: String? = null,
    override var maxSdk: Int? = null,
    override val name: String, override val testApplicationId: String? = null,
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
    override val applicationIdSuffix: String? = null,
    override val versionNameSuffix: String? = null,
    override val multiDexEnabled: Boolean? = null,
    override val signingConfig: DefaultSigningConfig? = null,
    override val consumerProguardFiles: List<String>? = null,
    override val aarMetadata: AarMetadata? = null,

    ) : ApplicationDefaultConfig,
    DynamicFeatureDefaultConfig,
    LibraryDefaultConfig,
    TestDefaultConfig {

    context(Project)
    override fun applyTo(dimension: VariantDimension) {
        super<ApplicationDefaultConfig>.applyTo(dimension)
        super<DynamicFeatureDefaultConfig>.applyTo(dimension)
        super<LibraryDefaultConfig>.applyTo(dimension)
        super<TestDefaultConfig>.applyTo(dimension)
    }
}
