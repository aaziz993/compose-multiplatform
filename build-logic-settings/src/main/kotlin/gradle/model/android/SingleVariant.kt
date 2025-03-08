package gradle.model.android

/**
 * Single variant publishing options.
 */
internal interface SingleVariant : PublishingOptions {

    val variantName: String
}
