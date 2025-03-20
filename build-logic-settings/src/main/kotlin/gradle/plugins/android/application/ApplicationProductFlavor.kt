package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationProductFlavor
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.android.BuildConfigField
import gradle.plugins.android.ExternalNativeBuildFlags
import gradle.plugins.android.compile.JavaCompileOptions
import gradle.plugins.android.MissingDimensionStrategy
import gradle.plugins.android.Ndk
import gradle.plugins.android.Optimization
import gradle.plugins.android.flavor.ProductFlavorDsl
import gradle.plugins.android.ResValue
import gradle.plugins.android.Shaders
import gradle.plugins.android.VectorDrawables
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Encapsulates all product flavors properties for application projects.
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
internal interface ApplicationProductFlavor<T : ApplicationProductFlavor> :
    ApplicationBaseFlavor<T>,
    ProductFlavorDsl<T> {

    /** Whether this product flavor should be selected in Studio by default  */
    val isDefault: Boolean?

    context(Project)
    override fun applyTo(recipient: T) {
        super<ApplicationBaseFlavor>.applyTo(recipient)
        super<ProductFlavorDsl>.applyTo(recipient)

        recipient::isDefault trySet isDefault
    }
}

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
    override val dimension: String? = null,
    override val matchingFallbacks: List<String>? = null,
    /** Whether this product flavor should be selected in Studio by default  */
    override val isDefault: Boolean? = null,
) : gradle.plugins.android.application.ApplicationProductFlavor<ApplicationProductFlavor>

internal object ApplicationProductFlavorTransformingSerializer : KeyTransformingSerializer<ApplicationProductFlavorImpl>(
    ApplicationProductFlavorImpl.serializer(),
    "name",
)

