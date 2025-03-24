package gradle.plugins.android.split

import com.android.build.api.dsl.Splits

/**
 * Options to configure Multiple APKs.
 *
 * [`android`][ApplicationExtension.splits]`.`[`splits`][SplitsDsl]
 *
 * If you publish your app to Google Play, you should build and upload an
 * [Android App Bundle](https://developer.android.com/guide/app-bundle).
 * When you do so, Google Play automatically generates and serves optimized APKs for each user’s
 * device configuration, so they download only the code and resources they need to run your app.
 * This is much simpler than managing multiple APKs manually.
 *
 * If you publish your app to a store that doesn't support the Android App Bundle format, you can
 * publish
 * [multiple APKs](https://developer.android.com/studio/build/configure-apk-splits.html)
 * manually.
 *
 * The Android Gradle plugin supports generating multiple APKs based on screen density and
 * [Application Binary Interface (ABI)](https://developer.android.com/ndk/guides/abis.html),
 * where each APK contains the code and resources required for a given device configuration.
 *
 * You will also need to
 * [assign version codes to each APK](https://developer.android.com/studio/build/configure-apk-splits.html#configure-APK-versions)
 * so that you are able to manage updates later.
 *
 * Previously the Android Gradle plugin also supported building 'Configuration APKs' for Instant
 * Apps using this `splits` block, but that has been superseded by the Android App Bundle format.
 */
internal interface SplitsDsl<T : Splits> {

    /**
     * Encapsulates settings for
     * [building per-ABI APKs](https://developer.android.com/studio/build/configure-apk-splits.html#configure-abi-split).
     */
    val abi: AbiSplit<out com.android.build.api.dsl.AbiSplit>?

    fun applyTo(receiver: T) {
        (abi as AbiSplit<com.android.build.api.dsl.AbiSplit>?)?.applyTo(receiver.abi)
    }
}
