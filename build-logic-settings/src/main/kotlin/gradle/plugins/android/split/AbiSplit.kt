package gradle.plugins.android.split

import com.android.build.api.dsl.AbiSplit
import klib.data.type.reflection.trySet

/**
 * DSL object for configuring per-abi splits options.
 *
 * See [FULL_APK Splits](https://developer.android.com/studio/build/configure-apk-splits.html).
 */
internal interface AbiSplit<T : AbiSplit> : Split<T> {

    /** Whether to create an FULL_APK with all available ABIs. */
    val isUniversalApk: Boolean?

    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::isUniversalApk trySet isUniversalApk
    }
}
