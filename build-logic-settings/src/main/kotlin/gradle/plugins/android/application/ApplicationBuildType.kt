package gradle.plugins.android.application

import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.android.BuildConfigField
import gradle.plugins.android.BuildType
import gradle.plugins.android.ExternalNativeBuildFlags
import gradle.plugins.android.compile.JavaCompileOptions
import gradle.plugins.android.Ndk
import gradle.plugins.android.Optimization
import gradle.plugins.android.PostProcessing
import gradle.plugins.android.ResValue
import gradle.plugins.android.Shaders
import gradle.plugins.android.VcsInfo
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Build types define certain properties that Gradle uses when building and packaging your app, and
 * are typically configured for different stages of your development lifecycle.
 *
 * There are two build types defined by default, `debug` and `release`, and you can customize them
 * and create additional build types.
 *
 * The default debug build type enables debug options and signs the APK with the debug
 * key, while the release build type is not debuggable and can be configured to shrink, obfuscate,
 * and sign your APK with a release key for distribution.
 *
 * See
 * [configuring build types](https://developer.android.com/studio/build#build-config)
 * for more information.
 */
@Serializable
internal data class ApplicationBuildType(
    override val enableUnitTestCoverage: Boolean? = null,
    override val enableAndroidTestCoverage: Boolean? = null,
    override val isPseudoLocalesEnabled: Boolean? = null,
    override val isJniDebuggable: Boolean? = null,
    override val renderscriptOptimLevel: Int? = null,
    override val isMinifyEnabled: Boolean? = null,
    override val isShrinkResources: Boolean? = null,
    override val matchingFallbacks: List<String>? = null,
    override val postprocessing: PostProcessing? = null,
    override val initWith: String? = null,
    override val vcsInfo: VcsInfo? = null,
    override val name: String, override val multiDexKeepProguard: String? = null,
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
    /** Whether this build type should generate a debuggable apk. */
    val isDebuggable: Boolean? = null,
    /**
     * Whether a linked Android Wear app should be embedded in variant using this build type.
     *
     * Wear apps can be linked with the following code:
     *
     * ```
     * dependencies {
     *     freeWearApp project(:wear:free') // applies to variant using the free flavor
     *     wearApp project(':wear:base') // applies to all other variants
     * }
     * ```
     */
    val isEmbedMicroApp: Boolean? = null,
    /**
     * Whether to crunch PNGs.
     *
     * Setting this property to `true` reduces of PNG resources that are not already
     * optimally compressed. However, this process increases build times.
     *
     * PNG crunching is enabled by default in the release build type and disabled by default in
     * the debug build type.
     */
    val isCrunchPngs: Boolean? = null,
    /** Whether this product flavor should be selected in Studio by default  */
    val isDefault: Boolean? = null,
    /**
     * Intended to produce an APK that leads to more accurate profiling.
     *
     * Enabling this option will declare the application as profileable in the AndroidManifest.
     *
     * Profileable build types will be signed with the default debug signing config if no other
     * signing config is specified.
     *
     * This option doesn't make sense to combine with isDebuggable=true.
     * If a build type is set to be both debuggable and profileable the build system will log a
     * warning.
     */
    val isProfileable: Boolean? = null,
) : BuildType<com.android.build.api.dsl.ApplicationBuildType>,
    ApplicationVariantDimension<com.android.build.api.dsl.ApplicationBuildType> {

    context(Project)
    override fun applyTo(receiver: com.android.build.api.dsl.ApplicationBuildType) {
        super<BuildType>.applyTo(receiver)

        receiver::isDebuggable trySet isDebuggable
        receiver::isEmbedMicroApp trySet isEmbedMicroApp
        receiver::isCrunchPngs trySet isCrunchPngs
        receiver::isDefault trySet isDefault
        receiver::isProfileable trySet isProfileable
    }
}

internal object ApplicationBuildTypeTransformingSerializer : KeyTransformingSerializer<ApplicationBuildType>(
    ApplicationBuildType.serializer(),
    "name",
)
