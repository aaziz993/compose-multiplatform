package gradle.model.android

import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.gradle.internal.dsl.BuildType
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/** DSL object to configure build types.  */
@Serializable
internal data class BuildType(
    /**
     * Name of this build type.
     */
    val name: String,
    val lazyInit: Boolean? = null,
    val enableAndroidTestCoverage: Boolean? = null,
    val enableUnitTestCoverage: Boolean? = null,
    val isDebuggable: Boolean? = null,
    val isTestCoverageEnabled: Boolean? = null,
    val isPseudoLocalesEnabled: Boolean? = null,
    val isJniDebuggable: Boolean? = null,
    val isRenderscriptDebuggable: Boolean? = null,
    val renderscriptOptimLevel: Int? = null,
    val isProfileable: Boolean? = null,
    val ndk: Ndk? = null,
    /**
     * Configure native build options.
     */
    val externalNativeBuildOptions: ExternalNativeBuildFlags? = null,

    /*
     * (Non javadoc): Whether png crunching should be enabled if not explicitly overridden.
     *
     * Can be removed once the AaptOptions crunch method is removed.
     */
    val isCrunchPngsDefault: Boolean? = null,

    val matchingFallbacks: List<String>? = null,

    val javaCompileOptions: JavaCompileOptions? = null,

    /**
     * Configure shader compiler options for this build type.
     */
    val shaders: Shaders? = null,

    /** The signing configuration. e.g.: `signingConfig signingConfigs.myConfig`  */
    val signingConfig: ApkSigningConfig? = null,

    val isEmbedMicroApp: Boolean? = null,

    val isDefault: Boolean? = null,

    val proguardFiles: List<String>? = null,

    /**
     * Specifies proguard rule files to be used when processing test code.
     *
     *
     * Test code needs to be processed to apply the same obfuscation as was done to main code.
     */
    val testProguardFiles: List<String>? = null,


    /**
     * Specifies a proguard rule file to be included in the published AAR.
     *
     *
     * This proguard rule file will then be used by any application project that consume the AAR
     * (if proguard is enabled).
     *
     *
     * This allows AAR to specify shrinking or obfuscation exclude rules.
     *
     *
     * This is only valid for Library project. This is ignored in Application project.
     */
    val consumerProguardFiles: List<String>? = null,


    val isMinifyEnabled: Boolean? = null,

    /**
     * Whether shrinking of unused resources is enabled.
     *
     * Default is false;
     */
    val isShrinkResources: Boolean? = null,


    val isCrunchPngs: Boolean? = null,

    val postProcessingBlockUsed: Boolean? = null,

    /** This DSL is incubating and subject to change.  */
    val postprocessing: PostProcessing? = null,
) {
    context(Project)
    @Suppress("UnstableApiUsage")
    fun applyTo(buildType: BuildType) {
        lazyInit?.takeIf { it }?.run { buildType.lazyInit() }
        buildType::enableAndroidTestCoverage trySet enableAndroidTestCoverage
        buildType::enableUnitTestCoverage trySet enableUnitTestCoverage
        buildType::isDebuggable trySet isDebuggable
        buildType::isTestCoverageEnabled trySet isTestCoverageEnabled
        buildType::isPseudoLocalesEnabled trySet isPseudoLocalesEnabled
        buildType::isJniDebuggable trySet isJniDebuggable
        buildType::isRenderscriptDebuggable trySet isRenderscriptDebuggable
        buildType::renderscriptOptimLevel trySet renderscriptOptimLevel
        buildType::isProfileable trySet isProfileable
        buildType.ndk.abiFilters

        ndk?.let { ndkConfig ->
            buildType.ndk(ndkConfig::applyTo)
        }

        externalNativeBuildOptions?.let { externalNativeBuildOptions ->
            buildType.externalNativeBuild(externalNativeBuildOptions::applyTo)
        }

        buildType::isCrunchPngsDefault trySet isCrunchPngsDefault

        matchingFallbacks?.let(buildType.matchingFallbacks::addAll)

        javaCompileOptions?.let { javaCompileOptions ->
            buildType.javaCompileOptions(javaCompileOptions::applyTo)
        }

        shaders?.let { shaders ->
            buildType.shaders(shaders::applyTo)
        }

        buildType::signingConfig trySet signingConfig
        buildType::isEmbedMicroApp trySet isEmbedMicroApp
        buildType::isDefault trySet isDefault

        proguardFiles?.let { proguardFiles ->
            buildType.proguardFiles(*proguardFiles.toTypedArray())
        }

        testProguardFiles?.let { testProguardFiles ->
            buildType.testProguardFiles(*testProguardFiles.toTypedArray())
        }

        consumerProguardFiles?.let { consumerProguardFiles ->
            buildType.consumerProguardFiles(*consumerProguardFiles.toTypedArray())
        }

        buildType::isMinifyEnabled trySet isMinifyEnabled
        buildType::isShrinkResources trySet isShrinkResources
        buildType::isCrunchPngs trySet isCrunchPngs
        buildType::postProcessingBlockUsed trySet postProcessingBlockUsed

        postprocessing?.let { postprocessing ->
            buildType.postprocessing(postprocessing::applyTo)
        }
    }
}

