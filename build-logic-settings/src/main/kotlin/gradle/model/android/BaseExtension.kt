package gradle.model.android

import gradle.androidNamespace
import gradle.libs
import gradle.maybeNamed
import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import gradle.settings
import gradle.trySet
import gradle.version
import gradle.versions
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project

@Serializable(with = BaseExtensionSerializer::class)
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
    fun applyTo(extension: com.android.build.gradle.BaseExtension) {
        composeOptions?.let { composeOptions ->
            extension.composeOptions {
                ::kotlinCompilerExtensionVersion trySet composeOptions.kotlinCompilerExtensionVersion
            }
        }

        dataBinding?.let { dataBinding ->
            extension.dataBinding(dataBinding::applyTo)
        }

        viewBinding?.let { viewBinding ->
            extension.viewBinding(viewBinding::applyTo)
        }

        defaultPublishConfig?.let(extension::defaultPublishConfig)
        disableWrite?.takeIf { it }?.run { extension.disableWrite() }
        (compileSdkVersion ?: settings.libs.versions.version("android.compileSdk")?.toInt())
            ?.let(extension::compileSdkVersion)
        buildToolsVersion?.let(extension::buildToolsVersion)
        flavorDimensions?.let { flavorDimensions ->
            extension.flavorDimensions(*flavorDimensions.toTypedArray())
        }

        aaptOptions?.let { aaptOptions ->
            extension.aaptOptions(aaptOptions::applyTo)
        }

        externalNativeBuild?.let { externalNativeBuild ->
            extension.externalNativeBuild {
                externalNativeBuild.applyTo(this)
            }
        }

        testOptions?.let { testOptions ->
            extension.testOptions {
                testOptions.applyTo(this)
            }
        }

        compileOptions?.let { compileOptions ->
            extension.compileOptions {
                compileOptions.applyTo(this)
            }
        }

        packaging?.let { packagingOptions ->
            extension.packagingOptions(packagingOptions::applyTo)
        }

        adbOptions?.let { adbOptions ->
            extension.adbOptions(adbOptions::applyTo)
        }

        splits?.let { splits ->
            extension.splits(splits::applyTo)
        }

        extension::generatePureSplits trySet generatePureSplits
        flavorDimensions?.let(extension.flavorDimensionList::addAll)
        resourcePrefix?.let(extension::resourcePrefix)
        extension::ndkVersion trySet ndkVersion
        extension::ndkPath trySet ndkPath
        libraryRequests?.map(LibraryRequest::toLibraryRequest)?.let(extension.libraryRequests::addAll)

        buildTypes?.forEach { buildType ->
            extension.buildTypes {
                maybeNamed(buildType.name) {
                    buildType.applyTo(this as Named)
                } ?: create(buildType.name) {
                    buildType.applyTo(this as Named)
                }
            }
        }

        defaultConfig?.let { defaultConfig ->
            extension.defaultConfig {
                defaultConfig.applyTo(this)
            }
        }

        productFlavors?.forEach { productFlavors ->
            extension.productFlavors {
                productFlavors.applyTo(this)
            }
        }

        signingConfigs?.forEach { signingConfig ->
            signingConfig.applyTo(extension.signingConfigs)
        }

        buildFeatures?.applyTo(extension.buildFeatures)

        extension.namespace = namespace ?: androidNamespace
    }
}

private object BaseExtensionSerializer : JsonContentPolymorphicSerializer<BaseExtension>(
    BaseExtension::class,
)
