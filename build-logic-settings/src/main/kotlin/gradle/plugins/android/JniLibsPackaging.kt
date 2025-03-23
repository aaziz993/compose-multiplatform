package gradle.plugins.android

import com.android.build.api.dsl.JniLibsPackaging
import gradle.api.trySet
import gradle.collection.act
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
    val setExcludes: Set<String>? = null,
    /**
     * The set of patterns where the first occurrence is packaged in the APK. For each native
     * library APK entry path matching one of these patterns, only the first native library found
     * with that path gets packaged.
     *
     * Example: `android.packagingOptions.jniLibs.pickFirsts += "**`/`pickFirst.so"`
     */
    val pickFirsts: Set<String>? = null,
    val setPickFirsts: Set<String>? = null,
    /**
     * The set of patterns for native libraries that should not be stripped of debug symbols.
     *
     * Example: `android.packagingOptions.jniLibs.keepDebugSymbols += "**`/`doNotStrip.so"`
     */
    val keepDebugSymbols: Set<String>? = null,
    val setKeepDebugSymbols: Set<String>? = null,
    /**
     * The set of test-only patterns. Native libraries matching any of these patterns do not get
     * packaged in the main APK or AAR, but they are included in the test APK.
     *
     * Example: `android.packaging.jniLibs.testOnly += "**`/`testOnly.so"`
     */
    val testOnly: Set<String>? = null,
    val setTestOnly: Set<String>? = null,
) {

    fun applyTo(receiver: JniLibsPackaging) {
        receiver::useLegacyPackaging trySet useLegacyPackaging
        excludes?.let(receiver.excludes::addAll)
        setExcludes?.act(receiver.excludes::clear)?.let(receiver.excludes::addAll)
        pickFirsts?.let(receiver.pickFirsts::addAll)
        setPickFirsts?.act(receiver.pickFirsts::clear)?.let(receiver.pickFirsts::addAll)
        keepDebugSymbols?.let(receiver.keepDebugSymbols::addAll)
        setKeepDebugSymbols?.act(receiver.keepDebugSymbols::clear)?.let(receiver.keepDebugSymbols::addAll)
        testOnly?.let(receiver.testOnly::addAll)
        setTestOnly?.act(receiver.testOnly::clear)?.let(receiver.testOnly::addAll)
    }
}
