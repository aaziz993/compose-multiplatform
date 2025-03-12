package gradle.plugins.android

import gradle.android
import gradle.androidNamespace
import gradle.libs
import gradle.settings
import gradle.trySet
import gradle.version
import gradle.versions
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal interface BaseExtension {

    val composeOptions: ComposeOptions?

    val dataBinding: DataBinding?

    val viewBinding: ViewBinding?

    val defaultPublishConfig: String?

    val disableWrite: Boolean?

    /** For groovy only (so `compileSdkVersion=2` works) */
    val compileSdkVersion: Int?

    val buildToolsVersion: String?

    val flavorDimensions: List<String>?

    val aaptOptions: AaptOptions?

    val externalNativeBuild: ExternalNativeBuild?

    /**
     * Specifies options for how the Android plugin should run local and instrumented tests.
     *
     * For more information about the properties you can configure in this block, see [TestOptions].
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

    val libraryRequests: List<LibraryRequest>?

    val buildTypes: List<BuildType>?

    val defaultConfig: DefaultConfigDsl?

    val productFlavors: List<ProductFlavorDsl>?

    val signingConfigs: List<@Serializable(with = SigningConfigTransformingSerializer::class) SigningConfigImpl>?

    // these are indirectly implemented by extensions when they implement the new public
    // extension interfaces via delegates.
    val buildFeatures: BuildFeatures?
    val namespace: String?

    context(Project)
    @Suppress("UnstableApiUsage")
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
        flavorDimensions?.let(android.flavorDimensionList::addAll)
        resourcePrefix?.let(android::resourcePrefix)
        android::ndkVersion trySet ndkVersion
        android::ndkPath trySet ndkPath
        libraryRequests?.map(LibraryRequest::toLibraryRequest)?.let(android.libraryRequests::addAll)

        buildTypes?.forEach { buildType ->
            buildType.applyTo(android.buildTypes, android.buildTypes::create)
        }

        defaultConfig?.applyTo(android.defaultConfig)

        productFlavors?.forEach { productFlavors ->
            productFlavors.applyTo(android.productFlavors, android.productFlavors::create)
        }

        signingConfigs?.forEach { signingConfig ->
            signingConfig.applyTo(android.signingConfigs, android.signingConfigs::create)
        }

        buildFeatures?.applyTo(android.buildFeatures)
        android.namespace = namespace ?: androidNamespace
    }
}
