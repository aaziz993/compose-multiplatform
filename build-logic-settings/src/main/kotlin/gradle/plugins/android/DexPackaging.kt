package gradle.plugins.android

import com.android.build.api.dsl.DexPackaging
import gradle.trySet
import kotlinx.serialization.Serializable

/**
 * Packaging options for Dex (Android Dalvik Executable) files in the Android DSL
 *
 * ```kotlin
 * android {
 *     packaging {
 *         dex {
 *             useLegacyPackaging = ...
 *         }
 *     }
 * }
 * ```
 *
 */
@Serializable
internal data class DexPackaging(
    /**
     * Whether to use the legacy convention of compressing all dex files in the APK. If null, dex
     * files will be uncompressed when minSdk >= 28.
     *
     * This property does not affect dex file compression in APKs produced from app bundles.
     */
    val useLegacyPackaging: Boolean? = null
) {

    fun applyTo(packaging: DexPackaging) {
        packaging::useLegacyPackaging trySet useLegacyPackaging
    }
}
