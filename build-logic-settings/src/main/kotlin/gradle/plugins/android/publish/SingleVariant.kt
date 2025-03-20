package gradle.plugins.android.publish

import com.android.build.api.dsl.SingleVariant

/**
 * Single variant publishing options.
 */
internal interface SingleVariant<T : SingleVariant> : PublishingOptions<T> {

    val variantName: String
}
