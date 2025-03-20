package gradle.plugins.android

import com.android.build.api.dsl.NdkBuildFlags
import gradle.collection.act
import kotlinx.serialization.Serializable

/**
 * DSL object for per-variant ndk-build options, such as ndk-build arguments and compiler flags.
 *
 * To learn more about the ndk-build toolchain, read the official NDK documentation about
 * [Building Your Project](https://developer.android.com/ndk/guides/build.html).
 */
@Serializable
internal data class NdkBuildFlags(
    /**
     * Specifies arguments for ndk-build.
     *
     * The properties you can configure are the same as those available in your
     * [Android.mk](https://developer.android.com/ndk/guides/android_mk.html) and
     * [Application.mk](https://developer.android.com/ndk/guides/application_mk.html)
     * scripts. The following sample specifies the `Application.mk` for the ndk-build project:
     *
     * ```
     * android {
     *     // Similar to other properties in the defaultConfig block, you can override
     *     // these properties for each product flavor you configure.
     *     defaultConfig {
     *         externalNativeBuild {
     *             ndkBuild {
     *                 // Passes an optional argument to ndk-build.
     *                 arguments "NDK_MODULE_PATH+=../../third_party/modules"
     *             }
     *         }
     *     }
     * }
     * ```
     *
     * By default, this property is `null`.
     *
     * since 2.2.0
     */
    val arguments: List<String>? = null,
    val setArguments: List<String>? = null,
    /**
     * Specifies flags for the C compiler.
     *
     * The following sample enables format macro constants:
     *
     * ```
     * android {
     *     // Similar to other properties in the defaultConfig block, you can override
     *     // these properties for each product flavor in your build configuration.
     *     defaultConfig {
     *         externalNativeBuild {
     *             ndkBuild {
     *                 // Sets an optional flag for the C compiler.
     *                 cFlags "-D__STDC_FORMAT_MACROS"
     *             }
     *         }
     *     }
     * }
     * ```
     *
     * By default, this property is `null`.
     *
     * since 2.2.0
     */
    val setCFlags: List<String>? = null,
    val cFlags: List<String>? = null,
    /**
     * Specifies flags for the C++ compiler.
     *
     * The following sample enables RTTI (RunTime Type Information) support and C++ exceptions:
     *
     * ```
     * android {
     *     // Similar to other properties in the defaultConfig block, you can override
     *     // these properties for each product flavor in your build configuration.
     *     defaultConfig {
     *         externalNativeBuild {
     *             ndkBuild {
     *                 // Sets optional flags for the C++ compiler.
     *                 cppFlags "-fexceptions", "-frtti"
     *             }
     *         }
     *     }
     * }
     * ```
     *
     * By default, this property is `null`.
     *
     * since 2.2.0
     */
    val cppFlags: List<String>? = null,
    val setCppFlags: List<String>? = null,
    /**
     * Specifies the Application Binary Interfaces (ABI) that Gradle should build outputs for. The
     * ABIs that Gradle packages into your APK are determined by
     * [android.defaultConfig.ndk.abiFilter][Ndk.abiFilters]
     *
     * In most cases, you need to specify ABIs using only
     * [android.defaultConfig.ndk.abiFilter][Ndk.abiFilters], because it tells Gradle which ABIs to
     * both build and package into your APK. However, if you want to control what Gradle should
     * build, independently of what you want it to package into your APK, configure this property
     * with the ABIs you want Gradle to build.
     *
     * To further reduce the size of your APK, consider
     * [configuring multiple APKs based on ABI](https://developer.android.com/studio/build/configure-apk-splits.html#configure-abi-split)—instead
     * of creating one large APK with all versions of your native libraries, Gradle creates a
     * separate APK for each ABI you want to support and only packages the files each ABI needs.
     *
     * By default, this property is `null`.
     *
     * since 2.2.0
     */
    val abiFilters: Set<String>? = null,
    val setAbiFilters: Set<String>? = null,
    /**
     * Specifies the library and executable targets from your ndk-build project that Gradle should
     * build.
     *
     * For example, if your ndk-build project defines multiple libraries and executables, you can
     * tell Gradle to build only a subset of those outputs as follows:
     *
     * ```
     * android {
     *     // Similar to other properties in the defaultConfig block, you can override
     *     // these properties for each product flavor in your build configuration.
     *     defaultConfig {
     *         externalNativeBuild {
     *             ndkBuild {
     *                 // The following tells Gradle to build only the "libexample-one.so" and
     *                 // "my-executible-two" targets from the linked ndk-build project. If you don't
     *                 // configure this property, Gradle builds all executables and shared object
     *                 // libraries that you define in your ndk-build project. However, by default,
     *                 // Gradle packages only the shared libraries in your APK.
     *                 targets "libexample-one.so",
     *                         // You need to configure this executable and its sources in your
     *                         // Android.mk file like you would any other library, except you must
     *                         // specify "include $(BUILD_EXECUTABLE)". Building executables from
     *                         // your native sources is optional, and building native libraries to
     *                         // package into your APK satisfies most project requirements.
     *                         "my-executible-demo"
     *             }
     *         }
     *     }
     * }
     * ```
     *
     * If you don't configure this property, Gradle builds all executables and shared object
     * libraries that you define in your ndk-build project. However, by default, Gradle packages
     * only the shared libraries in your APK.
     *
     * since 2.2.0
     */
    val targets: Set<String>? = null,
    val setTargets: Set<String>? = null
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(recipient: NdkBuildFlags) {
        arguments?.let(recipient.arguments::addAll)
        setArguments?.act(recipient.arguments::clear)?.let(recipient.arguments::addAll)
        cFlags?.let(recipient.cFlags::addAll)
        setCFlags?.act(recipient.cFlags::clear)?.let(recipient.cFlags::addAll)
        cppFlags?.let(recipient.cppFlags::addAll)
        setCppFlags?.act(recipient.cppFlags::clear)?.let(recipient.cppFlags::addAll)
        abiFilters?.let(recipient.abiFilters::addAll)
        setAbiFilters?.act(recipient.abiFilters::clear)?.let(recipient.abiFilters::addAll)
        targets?.let(recipient.targets::addAll)
        setTargets?.act(recipient.targets::clear)?.let(recipient.targets::addAll)
    }
}
