package gradle.model.kotlin.kmp.jvm.android

/**
 * Single variant publishing options.
 */
internal interface SingleVariant : PublishingOptions {

    val variantName: String
}
