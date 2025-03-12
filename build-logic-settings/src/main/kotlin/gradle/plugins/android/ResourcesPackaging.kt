package gradle.plugins.android

import com.android.build.api.dsl.ResourcesPackaging
import kotlinx.serialization.Serializable

/**
 * Packaging options for java resource files in the Android DSL
 *
 * ```kotlin
 * android {
 *     packaging {
 *         resources {
 *             excludes += "/..."
 *         }
 *     }
 * }
 * ```
 */
@Serializable
internal data class ResourcesPackaging(
    /**
     * The set of excluded patterns. Java resources matching any of these patterns do not get
     * packaged in the APK.
     *
     * Example: `android.packagingOptions.resources.excludes += "**`/`*.exclude"`
     */
    val excludes: Set<String>? = null,

    /**
     * The set of patterns for which the first occurrence is packaged in the APK. For each java
     * resource APK entry path matching one of these patterns, only the first java resource found
     * with that path gets packaged in the APK.
     *
     * Example: `android.packagingOptions.resources.pickFirsts += "**`/`*.pickFirst"`
     */
    val pickFirsts: Set<String>? = null,

    /**
     * The set of patterns for which matching java resources are merged. For each java resource
     * APK entry path matching one of these patterns, all java resources with that path are
     * concatenated and packaged as a single entry in the APK.
     *
     * Example: `android.packagingOptions.resources.merges += "**`/`*.merge"`
     */
    val merges: Set<String>? = null
) {

    fun applyTo(packaging: ResourcesPackaging) {
        excludes?.let(packaging.excludes::addAll)
        pickFirsts?.let(packaging.pickFirsts::addAll)
        merges?.let(packaging.merges::addAll)
    }
}
