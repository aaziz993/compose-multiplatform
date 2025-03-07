package gradle.model.android.application

import com.android.build.api.dsl.ApplicationAndroidResources
import gradle.model.android.AndroidResources
import gradle.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring Android resource options for Application plugins.
 *
 * This is accessed via [ApplicationExtension.androidResources]
 */
@Serializable
internal data class ApplicationAndroidResources(
    override val ignoreAssetsPattern: String? = null,
    override val ignoreAssetsPatterns: List<String>? = null,
    override val noCompress: List<String>? = null,
    override val failOnMissingConfigEntry: Boolean? = null,
    override val additionalParameters: List<String>? = null,
    override val namespaced: Boolean? = null,
    /**
     * Property that automatically generates locale config when enabled.
     */
    val generateLocaleConfig: Boolean? = null,
    /**
     * Specifies a list of locales that resources will be kept for.
     *
     * For example, if you are using a library that includes locale-specific resources (such as
     * AppCompat or Google Play Services), then your APK includes all translated language strings
     * for the messages in those libraries whether the rest of your app is translated to the same
     * languages or not. If you'd like to keep only the languages that your app officially supports,
     * you can specify those languages using the `localeFilters` property, as shown in the sample
     * below. Any resources for locales not specified are not included in the build.
     *
     * ````
     * android {
     *     androidResources {
     *         ...
     *         // Keeps language resources for only the locales specified below.
     *         localeFilters += listOf("en-rGB", "fr")
     *     }
     * }
     * ````
     *
     * The locale must be specified either as (1) a two-letter ISO 639-1 language code, with the
     * option to add a two-letter ISO 3166-1-alpha-2 region code preceded by "-r" (e.g. en-rUS), or
     * (2) a BCP-47 language tag, which additionally allows you to specify a script subtag
     * (e.g. b+sr+Latn+RS).
     *
     * For more information on formulating locale qualifiers, see
     * "Language, script (optional), and region (optional)" in the
     * [alternative resources](https://d.android.com/r/tools/alternative-resources) table.
     */
    val localeFilters: Set<String>? = null,
) : AndroidResources {

    @Suppress("UnstableApiUsage")
    override fun applyTo(resources: com.android.build.api.dsl.AndroidResources) {
        super.applyTo(resources)

        resources as ApplicationAndroidResources

        resources::generateLocaleConfig trySet generateLocaleConfig
        localeFilters?.let(resources.localeFilters::addAll)
    }
}
