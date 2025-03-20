package gradle.plugins.android

import com.android.build.api.dsl.SingleVariant

/**
 * Single variant publishing options.
 */
internal interface SingleVariant<in T : SingleVariant> : PublishingOptions<T> {

    val variantName: String
}
