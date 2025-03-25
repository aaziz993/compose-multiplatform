package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationVariantDimension
import gradle.accessors.android
import gradle.api.trySet
import gradle.plugins.android.VariantDimension
import org.gradle.api.Project

/**
 * Shared properties between DSL objects that contribute to an application variant.
 *
 * That is, [ApplicationBuildType] and [ApplicationProductFlavor] and [ApplicationDefaultConfig].
 */
internal interface ApplicationVariantDimension<T : ApplicationVariantDimension> : VariantDimension<T> {

    /**
     * Application id suffix. It is appended to the "base" application id when calculating the final
     * application id for a variant.
     *
     * In case there are product flavor dimensions specified, the final application id suffix
     * will contain the suffix from the default product flavor, followed by the suffix from product
     * flavor of the first dimension, second dimension and so on. All of these will have a dot in
     * between e.g. &quot;defaultSuffix.dimension1Suffix.dimensions2Suffix&quot;.
     */
    val applicationIdSuffix: String?

    /**
     * Version name suffix. It is appended to the "base" version name when calculating the final
     * version name for a variant.
     *
     * In case there are product flavor dimensions specified, the final version name suffix will
     * contain the suffix from the default product flavor, followed by the suffix from product
     * flavor of the first dimension, second dimension and so on.
     */
    val versionNameSuffix: String?

    /**
     * Returns whether multi-dex is enabled.
     *
     * This can be null if the flag is not set, in which case the default value is used.
     */
    val multiDexEnabled: Boolean?

    /** The associated signing config or null if none are set on the variant dimension. */
    val signingConfig: String?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::applicationIdSuffix trySet applicationIdSuffix
        receiver::versionNameSuffix trySet versionNameSuffix
        receiver::multiDexEnabled trySet multiDexEnabled
        receiver::signingConfig trySet signingConfig?.let(project.android.signingConfigs::getByName)
    }
}
