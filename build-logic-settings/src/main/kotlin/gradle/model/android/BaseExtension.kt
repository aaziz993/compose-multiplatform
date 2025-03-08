package gradle.model.android

import gradle.android
import gradle.androidNamespace
import gradle.libs
import gradle.maybeNamed
import gradle.settings
import gradle.trySet
import gradle.version
import gradle.versions
import org.gradle.api.Named
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

    val signingConfigs: List<SigningConfigImpl>?

    // these are indirectly implemented by extensions when they implement the new public
    // extension interfaces via delegates.
    val buildFeatures: BuildFeatures?
    val namespace: String?

    context(Project)
    @Suppress("UnstableApiUsage")
    fun applyTo() {
        composeOptions?.let { composeOptions ->
            android.composeOptions {
                ::kotlinCompilerExtensionVersion trySet composeOptions.kotlinCompilerExtensionVersion
            }
        }

        dataBinding?.let { dataBinding ->
            android.dataBinding(dataBinding::applyTo)
        }

        viewBinding?.let { viewBinding ->
            android.viewBinding(viewBinding::applyTo)
        }

        defaultPublishConfig?.let(android::defaultPublishConfig)
        disableWrite?.takeIf { it }?.run { android.disableWrite() }
        (compileSdkVersion ?: settings.libs.versions.version("android.compileSdk")?.toInt())
            ?.let(android::compileSdkVersion)
        buildToolsVersion?.let(android::buildToolsVersion)
        flavorDimensions?.let { flavorDimensions ->
            android.flavorDimensions(*flavorDimensions.toTypedArray())
        }

        aaptOptions?.let { aaptOptions ->
            android.aaptOptions(aaptOptions::applyTo)
        }

        externalNativeBuild?.let { externalNativeBuild ->
            android.externalNativeBuild {
                externalNativeBuild.applyTo(this)
            }
        }

        testOptions?.let { testOptions ->
            android.testOptions {
                testOptions.applyTo(this)
            }
        }

        compileOptions?.let { compileOptions ->
            android.compileOptions {
                compileOptions.applyTo(this)
            }
        }

        packaging?.let { packagingOptions ->
            android.packagingOptions(packagingOptions::applyTo)
        }

        adbOptions?.let { adbOptions ->
            android.adbOptions(adbOptions::applyTo)
        }

        splits?.let { splits ->
            android.splits(splits::applyTo)
        }

        android::generatePureSplits trySet generatePureSplits
        flavorDimensions?.let(android.flavorDimensionList::addAll)
        resourcePrefix?.let(android::resourcePrefix)
        android::ndkVersion trySet ndkVersion
        android::ndkPath trySet ndkPath
        libraryRequests?.map(LibraryRequest::toLibraryRequest)?.let(android.libraryRequests::addAll)

        buildTypes?.forEach { buildType ->
            android.buildTypes {
                maybeNamed(buildType.name) {
                    buildType.applyTo(this as Named)
                } ?: create(buildType.name) {
                    buildType.applyTo(this as Named)
                }
            }
        }

        defaultConfig?.let { defaultConfig ->
            android.defaultConfig {
                defaultConfig.applyTo(this)
            }
        }

        productFlavors?.forEach { productFlavors ->
            productFlavors.applyTo(android.productFlavors)
        }

        signingConfigs?.forEach { signingConfig ->
            signingConfig.applyTo(android.signingConfigs)
        }

        buildFeatures?.applyTo(android.buildFeatures)

        android.namespace = namespace ?: androidNamespace
    }
}
