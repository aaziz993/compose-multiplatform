package plugin.settings.model

/**
 * Configuration object for a build cache.
 *
 * @since 3.5
 */
internal interface BuildCache {

    /**
     * Sets whether the build cache is enabled.
     */
    val isEnabled: Boolean?

    /**
     * Sets whether a given build can store outputs in the build cache.
     */
    val isPush: Boolean?
}
