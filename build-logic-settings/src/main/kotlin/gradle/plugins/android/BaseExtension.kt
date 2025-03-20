package gradle.plugins.android

import gradle.accessors.android
import gradle.accessors.androidNamespace
import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.applyTo
import gradle.api.trySet
import gradle.collection.act
import gradle.plugins.android.compile.CompileOptions
import gradle.plugins.android.defaultconfig.DefaultConfigDsl
import gradle.plugins.android.features.BuildFeatures
import gradle.plugins.android.features.DataBinding
import gradle.plugins.android.features.ViewBinding
import gradle.plugins.android.flavor.ProductFlavorDsl
import gradle.plugins.android.signing.SigningConfigImpl
import gradle.plugins.android.signing.SigningConfigTransformingSerializer
import gradle.plugins.android.test.TestOptions
import java.util.*
import kotlin.collections.addAll
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal interface BaseExtension<
    BuildFeaturesT : com.android.build.api.dsl.BuildFeatures,
    BuildTypeT : com.android.build.api.dsl.BuildType,
    DefaultConfigT : com.android.build.api.dsl.DefaultConfig,
    ProductFlavorT : com.android.build.api.dsl.ProductFlavor,
    > {

    val composeOptions: ComposeOptions?

    val dataBinding: DataBinding?

    val viewBinding: ViewBinding?

    val defaultPublishConfig: String?

    val disableWrite: Boolean?

    /** For groovy only (so `compileSdkVersion=2` works) */
    val compileSdkVersion: Int?

    val buildToolsVersion: String?

    val flavorDimensions: SortedSet<String>?
    val setFlavorDimensions: SortedSet<String>?

    val aaptOptions: AaptOptions?

    val externalNativeBuild: ExternalNativeBuild?

    /**
     * Specifies options for how the Android plugin should run local and instrumented tests.
     *
     * For more information about the properties you can configure in this block, see [gradle.plugins.android.test.TestOptions].
     */
    val testOptions: TestOptions?

    val compileOptions: CompileOptions?

    val packaging: Packaging?

    /**
     * Specifies options for the
     * [Android Debug Bridge (ADB)](https://developer.android.com/studio/command-line/adb.html),
     * such as APK installation options.
     *
     * For more information about the properties you can configure in this block, see [AdbOptions].
     */
    val adbOptions: AdbOptions?

    val splits: Splits?

    // ---------------
    // TEMP for compatibility

    /** {@inheritDoc} */
    val generatePureSplits: Boolean?

    // Kept for binary and source compatibility until the old DSL interfaces can go away.
    val resourcePrefix: String?

    val ndkVersion: String?

    val ndkPath: String?

    val libraryRequests: Set<LibraryRequest>?

    val buildTypes: Set<BuildType<BuildTypeT>>?

    val defaultConfig: DefaultConfigDsl<DefaultConfigT>?

    val productFlavors: Set<ProductFlavorDsl<ProductFlavorT>>?

    val signingConfigs: Set<@Serializable(with = SigningConfigTransformingSerializer::class) SigningConfigImpl>?

    // these are indirectly implemented by extensions when they implement the new public
    // extension interfaces via delegates.
    val buildFeatures: BuildFeatures<BuildFeaturesT>?
    val namespace: String?

    context(Project)
    @Suppress("UnstableApiUsage", "UNCHECKED_CAST")
    fun applyTo() {
        composeOptions?.applyTo(android.composeOptions)
        dataBinding?.applyTo(android.dataBinding)
        viewBinding?.applyTo(android.viewBinding)
        defaultPublishConfig?.let(android::defaultPublishConfig)
        disableWrite?.takeIf { it }?.run { android.disableWrite() }
        (compileSdkVersion ?: settings.libs.versions.version("android.compileSdk")?.toInt())
            ?.let(android::compileSdkVersion)
        buildToolsVersion?.let(android::buildToolsVersion)

        flavorDimensions?.let { flavorDimensions ->
            android.flavorDimensions(*flavorDimensions.toTypedArray())
        }

        aaptOptions?.applyTo(android.aaptOptions)
        externalNativeBuild?.applyTo(android.externalNativeBuild)
        testOptions?.applyTo(android.testOptions)
        compileOptions?.applyTo(android.compileOptions)
        packaging?.applyTo(android.packagingOptions)
        adbOptions?.applyTo(android.adbOptions)
        splits?.applyTo(android.splits)
        android::generatePureSplits trySet generatePureSplits

        flavorDimensions?.let {  flavorDimensions ->
            android.flavorDimensions(*flavorDimensions.toTypedArray())
        }

        setFlavorDimensions?.act(android.flavorDimensionList::clear)?.let(android.flavorDimensionList::addAll)
        resourcePrefix?.let(android::resourcePrefix)
        android::ndkVersion trySet ndkVersion
        android::ndkPath trySet ndkPath
        libraryRequests?.map(LibraryRequest::toLibraryRequest)?.let(android.libraryRequests::addAll)

        buildTypes?.forEach { buildType ->
            buildType.applyTo(android.buildTypes as BuildTypeT)
        }

        defaultConfig?.applyTo(android.defaultConfig as DefaultConfigT)

        productFlavors?.forEach { productFlavors ->
            productFlavors.applyTo(android.productFlavors as ProductFlavorT)
        }

        signingConfigs?.forEach { signingConfig ->
            signingConfig.applyTo(android.signingConfigs)
        }

        buildFeatures?.applyTo(android.buildFeatures as BuildFeaturesT)
        android.namespace = namespace ?: androidNamespace
    }
}
