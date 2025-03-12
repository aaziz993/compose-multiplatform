package gradle.plugins.android

import com.android.build.api.dsl.JniLibsPackaging
import gradle.api.trySet
import kotlinx.serialization.Serializable

/**
 * Packaging options for native library (.so) files in the Android DSL
 *
 * ```kotlin
 * android {
 *     packaging {
 *         jniLibs {
 *             excludes += ...
 *         }
 *     }
 * }
 * ```
 */
@Serializable
internal data class JniLibsPackaging(
    /**
     * Whether to use the legacy convention of compressing all .so files in the APK. If null, .so
     * files will be uncompressed and page-aligned when minSdk >= 23.
     */
    val useLegacyPackaging: Boolean? = null,
    /**
     * The set of excluded patterns. Native libraries matching any of these patterns do not get
     * packaged.
     *
     * Example: `android.packagingOptions.jniLibs.excludes += "**`/`exclude.so"`
     */
    val excludes: Set<String>? = null,
    /**
     * The set of patterns where the first occurrence is packaged in the APK. For each native
     * library APK entry path matching one of these patterns, only the first native library found
     * with that path gets packaged.
     *
     * Example: `android.packagingOptions.jniLibs.pickFirsts += "**`/`pickFirst.so"`
     */
    val pickFirsts: Set<String>? = null,
    /**
     * The set of patterns for native libraries that should not be stripped of debug symbols.
     *
     * Example: `android.packagingOptions.jniLibs.keepDebugSymbols += "**`/`doNotStrip.so"`
     */
    val keepDebugSymbols: Set<String>? = null,
    /**
     * The set of test-only patterns. Native libraries matching any of these patterns do not get
     * packaged in the main APK or AAR, but they are included in the test APK.
     *
     * Example: `android.packaging.jniLibs.testOnly += "**`/`testOnly.so"`
     */
    val testOnly: Set<String>? = null,
) {

    fun applyTo(packaging: JniLibsPackaging) {
        packaging::useLegacyPackaging trySet useLegacyPackaging
        excludes?.let(packaging.excludes::addAll)
        pickFirsts?.let(packaging.pickFirsts::addAll)
        keepDebugSymbols?.let(packaging.keepDebugSymbols::addAll)
        testOnly?.let(packaging.testOnly::addAll)
    }
}
