package gradle.model.android

import com.android.build.api.dsl.SingleVariant

/**
 * Single variant publishing options.
 */
internal interface SingleVariant : PublishingOptions {

    val variantName: String
}
