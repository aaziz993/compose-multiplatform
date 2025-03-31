package gradle.plugins.android

import com.android.build.api.dsl.AndroidResources
import gradle.collection.tryAddAll
import gradle.collection.trySet
import gradle.reflect.trySet

/**
 * DSL object for configuring Android resource options.
 *
 * This is accessed via [CommonExtension.androidResources]
 */
internal interface AndroidResources<T : AndroidResources> {

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
    val ignoreAssetsPatterns: Set<String>?
    val setIgnoreAssetsPatterns: Set<String>?

    /**
     * File extensions of Android resources, assets, and Java resources to be stored uncompressed in
     * the APK. Adding an empty extension (e.g., setting `noCompress ''`) will disable compression
     * for all Android resources, assets, and Java resources.
     */
    val noCompress: Set<String>?
    val setNoCompress: Set<String>?

    /**
     * Forces aapt to return an error if it fails to find an entry for a configuration.
     *
     * See `aapt --help`
     */
    val failOnMissingConfigEntry: Boolean?

    /** List of additional parameters to pass to `aapt`. */
    val additionalParameters: List<String>?
    val setAdditionalParameters: List<String>?

    /**
     * Indicates whether the resources in this sub-project are fully namespaced.
     *
     * This property is incubating and may change in a future release.
     */
    val namespaced: Boolean?

    @Suppress("UnstableApiUsage")
    fun applyTo(receiver: T) {
        receiver::ignoreAssetsPattern trySet ignoreAssetsPattern
        receiver.ignoreAssetsPatterns tryAddAll ignoreAssetsPatterns
        receiver.ignoreAssetsPatterns trySet setIgnoreAssetsPatterns
        receiver.noCompress tryAddAll noCompress
        receiver.noCompress trySet setNoCompress
        receiver::failOnMissingConfigEntry trySet failOnMissingConfigEntry
        receiver.additionalParameters tryAddAll additionalParameters
        receiver.additionalParameters trySet setAdditionalParameters
        receiver::namespaced trySet receiver.namespaced
    }
}
