package gradle.plugins.android

import com.android.build.api.dsl.AndroidResources
import gradle.api.trySet
import gradle.collection.act

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
        ignoreAssetsPatterns?.let(receiver.ignoreAssetsPatterns::addAll)
        setIgnoreAssetsPatterns?.act(receiver.ignoreAssetsPatterns::clear)?.let(receiver.ignoreAssetsPatterns::addAll)
        noCompress?.let(receiver.noCompress::addAll)
        setNoCompress?.act(receiver.noCompress::clear)?.let(receiver.noCompress::addAll)
        receiver::failOnMissingConfigEntry trySet failOnMissingConfigEntry
        additionalParameters?.let(receiver.additionalParameters::addAll)
        setAdditionalParameters?.act(receiver.additionalParameters::clear)?.let(receiver.additionalParameters::addAll)
        receiver::namespaced trySet receiver.namespaced
    }
}
