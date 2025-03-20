package gradle.plugins.android

import com.android.build.api.dsl.AbiSplit
import gradle.api.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring per-abi splits options.
 *
 * See [FULL_APK Splits](https://developer.android.com/studio/build/configure-apk-splits.html).
 */
@Serializable
internal data class AbiSplit(
    override val isEnable: Boolean? = null,
    override val includes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val reset: Boolean? = null,
    /** Whether to create an FULL_APK with all available ABIs. */
    val isUniversalApk: Boolean? = null,
) : Split<AbiSplit> {

    override fun applyTo(recipient: AbiSplit) {
        super.applyTo(recipient)
        recipient::isUniversalApk trySet isUniversalApk
    }
}
