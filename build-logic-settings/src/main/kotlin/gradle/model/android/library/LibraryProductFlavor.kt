package gradle.model.android.library

import com.android.build.api.dsl.VariantDimension
import gradle.model.android.AarMetadata
import gradle.model.android.ApkSigningConfig
import gradle.model.android.BuildConfigField
import gradle.model.android.ExternalNativeBuildFlags
import gradle.model.android.JavaCompileOptions
import gradle.model.android.MissingDimensionStrategy
import gradle.model.android.Ndk
import gradle.model.android.Optimization
import gradle.model.android.ProductFlavorDsl
import gradle.model.android.ResValue
import gradle.model.android.Shaders
import gradle.model.android.VectorDrawables
import gradle.model.android.application.ApplicationProductFlavor
import gradle.serialization.serializer.AnySerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Encapsulates all product flavors properties for library projects.
 *
 * Product flavors represent different versions of your project that you expect to co-exist on a
 * single device, the Google Play store, or repository. For example, you can configure 'demo' and
 * 'full' product flavors for your app, and each of those flavors can specify different features,
 * device requirements, resources, and application ID's--while sharing common source code and
 * resources. So, product flavors allow you to output different versions of your project by simply
 * changing only the components and settings that are different between them.
 *
 * Configuring product flavors is similar to
 * [configuring build types](https://developer.android.com/studio/build/build-variants.html#build-types):
 * add them to the `productFlavors` block of your project's `build.gradle` file
 * and configure the settings you want.
 *
 * Product flavors support the same properties as the [gradle.model.android.DefaultConfigDsl] block—this is because
 * `defaultConfig` defines a [gradle.model.android.ProductFlavorDsl] object that the plugin uses as the base configuration
 * for all other flavors.
 * Each flavor you configure can then override any of the default values in `defaultConfig`, such as
 * the [`applicationId`](https://d.android.com/studio/build/application-id.html).
 *
 * When using Android plugin 3.0.0 and higher,
 * *[each flavor must belong to a `dimension`][dimension]*.
 *
 * When you configure product flavors, the Android plugin automatically combines them with your
 * [gradle.model.android.BuildType] configurations to
 * [create build variants](https://developer.android.com/studio/build/build-variants.html).
 * If the plugin creates certain build variants that you don't want, you can
 * [filter variants using `android.variantFilter`](https://developer.android.com/studio/build/build-variants.html#filter-variants).
 */
@Serializable
internal data class LibraryProductFlavor(
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
    override val signingConfig: ApkSigningConfig? = null,
    override val aarMetadata: AarMetadata? = null,
    override val dimension: String? = null,
    override val matchingFallbacks: List<String>? = null,
    /** Whether this product flavor should be selected in Studio by default  */
    val isDefault: Boolean?,
) : LibraryBaseFlavor,
    ProductFlavorDsl {

    context(Project)
    override fun applyTo(dimension: VariantDimension) {
        super<LibraryBaseFlavor>.applyTo(dimension)
        super<ProductFlavorDsl>.applyTo(dimension)

        dimension as com.android.build.api.dsl.LibraryProductFlavor

        dimension::isDefault trySet isDefault
    }
}

internal object LibraryProductFlavorTransformingSerializer : KeyTransformingSerializer<LibraryProductFlavor>(
    LibraryProductFlavor.serializer(),
    "name",
)
