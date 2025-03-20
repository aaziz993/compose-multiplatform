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
    fun applyTo(recipient: T) {
        recipient::ignoreAssetsPattern trySet ignoreAssetsPattern
        ignoreAssetsPatterns?.let(recipient.ignoreAssetsPatterns::addAll)
        setIgnoreAssetsPatterns?.act(recipient.ignoreAssetsPatterns::clear)?.let(recipient.ignoreAssetsPatterns::addAll)
        noCompress?.let(recipient.noCompress::addAll)
        setNoCompress?.act(recipient.noCompress::clear)?.let(recipient.noCompress::addAll)
        recipient::failOnMissingConfigEntry trySet failOnMissingConfigEntry
        additionalParameters?.let(recipient.additionalParameters::addAll)
        setAdditionalParameters?.act(recipient.additionalParameters::clear)?.let(recipient.additionalParameters::addAll)
        recipient::namespaced trySet recipient.namespaced
    }
}
