package gradle.plugins.android

import gradle.accessors.android
import gradle.accessors.androidNamespace
import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.act
import gradle.api.applyTo
import gradle.api.tryAddAll
import gradle.api.trySet
import gradle.ifTrue
import gradle.plugins.android.compile.CompileOptions
import gradle.plugins.android.defaultconfig.DefaultConfig
import gradle.plugins.android.features.BuildFeatures
import gradle.plugins.android.features.DataBinding
import gradle.plugins.android.features.ViewBinding
import gradle.plugins.android.flavor.ProductFlavor
import gradle.plugins.android.flavor.ProductFlavorKeyTransformingSerializer
import gradle.plugins.android.signing.SigningConfigImpl
import gradle.plugins.android.signing.SigningConfigKeyTransformingSerializer
import gradle.plugins.android.sourceset.AndroidSourceSet
import gradle.plugins.android.sourceset.AndroidSourceSetKeyTransformingSerializer
import gradle.plugins.android.split.Splits
import gradle.plugins.android.test.TestOptions
import gradle.serialization.serializer.JsonPolymorphicSerializer
import kotlinx.serialization.Serializable
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

    val flavorDimensions: LinkedHashSet<String>?
    val setFlavorDimensions: LinkedHashSet<String>?

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

    val sourceSets: LinkedHashSet<@Serializable(with = AndroidSourceSetKeyTransformingSerializer::class) AndroidSourceSet>?

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

    val setLibraryRequests: Set<LibraryRequest>?

    val buildTypes: LinkedHashSet<out BuildType<out com.android.build.api.dsl.BuildType>>?

    val defaultConfig: DefaultConfig?

    val productFlavors: LinkedHashSet<@Serializable(with = ProductFlavorKeyTransformingSerializer::class) ProductFlavor>?

    val signingConfigs: LinkedHashSet<@Serializable(with = SigningConfigKeyTransformingSerializer::class) SigningConfigImpl>?

    // these are indirectly implemented by extensions when they implement the new public
    // extension interfaces via delegates.
    val buildFeatures: BuildFeatures<out com.android.build.api.dsl.BuildFeatures>?
    val namespace: String?

    context(Project)
    @Suppress("UnstableApiUsage", "UNCHECKED_CAST")
    fun applyTo() {
        composeOptions?.applyTo(project.android.composeOptions)
        dataBinding?.applyTo(project.android.dataBinding)
        viewBinding?.applyTo(project.android.viewBinding)
        defaultPublishConfig?.let(project.android::defaultPublishConfig)
        disableWrite?.ifTrue(project.android::disableWrite)
        (compileSdkVersion ?: project.settings.libs.versions.version("project.android.compileSdk")?.toInt())
            ?.let(project.android::compileSdkVersion)
        buildToolsVersion?.let(project.android::buildToolsVersion)
        project.android::flavorDimensions trySet flavorDimensions
        aaptOptions?.applyTo(project.android.aaptOptions)
        externalNativeBuild?.applyTo(project.android.externalNativeBuild)
        testOptions?.applyTo(project.android.testOptions)
        compileOptions?.applyTo(project.android.compileOptions)
        packaging?.applyTo(project.android.packagingOptions)
        adbOptions?.applyTo(project.android.adbOptions)

        sourceSets?.forEach { sourceSet ->
            sourceSet.applyTo(project.android.sourceSets)
        }

        splits?.applyTo(project.android.splits)
        project.android::generatePureSplits trySet generatePureSplits
        project.android::flavorDimensions trySet flavorDimensions
        project.android.flavorDimensionList tryAddAll setFlavorDimensions
        resourcePrefix?.let(project.android::resourcePrefix)
        project.android::ndkVersion trySet ndkVersion
        project.android::ndkPath trySet ndkPath
        project.android.libraryRequests tryAddAll libraryRequests?.map(LibraryRequest::toLibraryRequest)
        project.android.libraryRequests trySet libraryRequests?.map(LibraryRequest::toLibraryRequest)

        buildTypes?.forEach { buildType ->
            (buildType as BuildType<com.android.build.api.dsl.BuildType>).applyTo(project.android.buildTypes)
        }

        defaultConfig?.applyTo(project.android.defaultConfig)

        productFlavors?.forEach { productFlavors ->
            productFlavors.applyTo(project.android.productFlavors)
        }

        signingConfigs?.forEach { signingConfig ->
            signingConfig.applyTo(project.android.signingConfigs)
        }

        (buildFeatures as BuildFeatures<com.android.build.api.dsl.BuildFeatures>?)?.applyTo(project.android.buildFeatures)
        project.android.namespace = namespace ?: project.androidNamespace
    }
}

private object BaseExtensionSerializer : JsonPolymorphicSerializer<BaseExtension>(
    BaseExtension::class,
)
