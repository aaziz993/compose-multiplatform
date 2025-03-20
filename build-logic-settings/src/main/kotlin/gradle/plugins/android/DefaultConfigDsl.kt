package gradle.plugins.android

import com.android.build.api.dsl.DefaultConfig

/**
 * Specifies defaults for variant properties that the Android plugin applies to all build variants.
 *
 * You can override any `defaultConfig` property when
 * [configuring product flavors](https://developer.android.com/studio/build/build-variants.html#product-flavors).
 * See [ProductFlavorDsl].
 *
 * Each plugin has its own interface that extends this one, see [gradle.model.android.application.ApplicationDefaultConfig],
 * [gradle.model.android.library.LibraryDefaultConfig], [DynamicFeatureDefaultConfig] and [TestDefaultConfig].
 */
internal interface DefaultConfigDsl<T : DefaultConfig> : BaseFlavor<T>
