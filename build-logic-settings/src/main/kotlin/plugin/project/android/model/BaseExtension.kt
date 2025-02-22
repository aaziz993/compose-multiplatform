package plugin.project.android.model

import com.android.build.gradle.BaseExtension
import gradle.maybeNamed
import gradle.trySet
import org.gradle.api.Project

internal interface BaseExtension {

    val composeOptions: ComposeOptions?

    val dataBinding: DataBindingOptions?

    val viewBinding: ViewBindingOptions?

    val defaultPublishConfig: String?

    val disableWrite: Boolean?

    /** For groovy only (so `compileSdkVersion=2` works) */
    val compileSdkVersion: Int?

    val buildToolsVersion: String?

    val flavorDimensions: List<String>?

    val aaptOptions: AaptOptions?

    val lintOptions: LintOptions?

    val externalNativeBuild: ExternalNativeBuild?

    /**
     * Specifies options for how the Android plugin should run local and instrumented tests.
     *
     * For more information about the properties you can configure in this block, see [TestOptions].
     */
    val testOptions: TestOptions?

    val compileOptions: CompileOptions?

    val packagingOptions: PackagingOptions?

    val jacoco: JacocoOptions?

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
    val flavorDimensionList: MutableList<String>?

    val resourcePrefix: String?

    val ndkVersion: String?

    val ndkPath: String?

    val libraryRequests: List<LibraryRequest>?

    val buildTypes: List<BuildType>?

//    val defaultConfig: DefaultConfig

//    val productFlavors: List<ProductFlavor>

    val signingConfigs: List<DefaultSigningConfig>?

    // these are indirectly implemented by extensions when they implement the new public
    // extension interfaces via delegates.
//    val buildFeatures: BuildFeatures
    val namespace: String?

    context(Project)
    @Suppress("UnstableApiUsage")
    fun applyTo(extension: BaseExtension) {
        composeOptions?.let { composeOptions ->
            extension.composeOptions {
                ::kotlinCompilerExtensionVersion trySet composeOptions.kotlinCompilerExtensionVersion
            }
        }

        dataBinding?.let { dataBinding ->
            extension.dataBinding {
                ::version trySet dataBinding.version
                ::addDefaultAdapters trySet dataBinding.addDefaultAdapters
                ::isEnabledForTests trySet dataBinding.isEnabledForTests
            }
        }

        viewBinding?.let { viewBinding ->
            extension.viewBinding {
                ::enable trySet viewBinding.enable
            }
        }

        defaultPublishConfig?.let(extension::defaultPublishConfig)
        disableWrite?.takeIf { it }?.run { extension.disableWrite() }
        compileSdkVersion?.let(extension::compileSdkVersion)
        buildToolsVersion?.let(extension::buildToolsVersion)
        flavorDimensions?.let { flavorDimensions ->
            extension.flavorDimensions(*flavorDimensions.toTypedArray())
        }

        aaptOptions?.let { aaptOptions ->
            extension.aaptOptions(aaptOptions::applyTo)
        }

        lintOptions?.let { lintOptions ->
            extension.lintOptions {
                lintOptions.applyTo(this)
            }
        }

        externalNativeBuild?.let { externalNativeBuild ->
            extension.externalNativeBuild {
                externalNativeBuild.applyTo(this)
            }
        }

        testOptions?.let { testOptions ->
            extension.testOptions(testOptions::applyTo)
        }

        compileOptions?.let { compileOptions ->
            extension.compileOptions(compileOptions::applyTo)
        }

        packagingOptions?.let { packagingOptions ->
            extension.packagingOptions(packagingOptions::applyTo)
        }

        jacoco?.let { jacoco ->
            extension.jacoco(jacoco::applyTo)
        }

        adbOptions?.let { adbOptions ->
            extension.adbOptions(adbOptions::applyTo)
        }

        splits?.let { splits ->
            extension.splits(splits::applyTo)
        }

        extension::generatePureSplits trySet generatePureSplits
        flavorDimensionList?.let(extension.flavorDimensionList::addAll)
        resourcePrefix?.let(extension::resourcePrefix)
        extension::ndkVersion trySet ndkVersion
        extension::ndkPath trySet ndkPath
        libraryRequests?.map(LibraryRequest::toLibraryRequest)?.let(extension.libraryRequests::addAll)

        buildTypes?.forEach { buildType ->
            extension.buildTypes {
                maybeNamed(buildType.name) {
                    buildType.applyTo(this)
                } ?: create(buildType.name) {
                    buildType.applyTo(this)
                }
            }
        }

//        extension.defaultConfig {
//            this.name=""
//        }

        extension.productFlavors {
            all {

            }
        }

        signingConfigs?.forEach { signingConfig ->
            extension.signingConfigs {
                maybeNamed(signingConfig.name) {
                    signingConfig.applyTo(this)
                } ?: create(signingConfig.name) {
                    signingConfig.applyTo(this)
                }
            }
        }

        extension::namespace trySet namespace
    }
}
