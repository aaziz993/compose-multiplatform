package gradle.plugins.android

import com.android.build.api.dsl.AndroidResources
import gradle.api.trySet

/**
 * DSL object for configuring Android resource options.
 *
 * This is accessed via [CommonExtension.androidResources]
 */
internal interface AndroidResources {

    /**
     * Pattern describing assets to be ignored.
     *
     * This is [ignoreAssetsPatterns] joined by ':'.
     */
    val ignoreAssetsPattern: String?

    /**
     * Patterns describing assets to be ignored.
     *
     * If empty, defaults to `["!.svn", "!.git", "!.ds_store", "!*.scc", ".*", "<dir>_*", "!CVS", "!thumbs.db", "!picasa.ini", "!*~"]`
     */
    val ignoreAssetsPatterns: List<String>?

    /**
     * File extensions of Android resources, assets, and Java resources to be stored uncompressed in
     * the APK. Adding an empty extension (e.g., setting `noCompress ''`) will disable compression
     * for all Android resources, assets, and Java resources.
     */
    val noCompress: List<String>?

    /**
     * Forces aapt to return an error if it fails to find an entry for a configuration.
     *
     * See `aapt --help`
     */
    val failOnMissingConfigEntry: Boolean?

    /** List of additional parameters to pass to `aapt`. */
    val additionalParameters: List<String>?

    /**
     * Indicates whether the resources in this sub-project are fully namespaced.
     *
     * This property is incubating and may change in a future release.
     */
    val namespaced: Boolean?

    @Suppress("UnstableApiUsage")
    fun applyTo(resources: AndroidResources) {
        resources::ignoreAssetsPattern trySet ignoreAssetsPattern
        ignoreAssetsPatterns?.let(resources.ignoreAssetsPatterns::addAll)
        noCompress?.let(resources.noCompress::addAll)
        resources::failOnMissingConfigEntry trySet failOnMissingConfigEntry
        additionalParameters?.let(resources.additionalParameters::addAll)
        resources::namespaced trySet resources.namespaced
    }
}
