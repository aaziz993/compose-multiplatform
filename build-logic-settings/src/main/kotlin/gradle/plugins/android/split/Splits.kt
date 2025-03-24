package gradle.plugins.android.split

import com.android.build.gradle.internal.dsl.Splits
import kotlinx.serialization.Serializable

@Serializable
internal data class Splits(
    override val abi: AbiSplitOptions? = null,
    /**
     * Encapsulates settings for
     * [building per-density APKs](https://developer.android.com/studio/build/configure-apk-splits.html#configure-density-split).
     */
    val density: DensitySplitOptions? = null,
) : SplitsDsl<Splits> {

    override fun applyTo(receiver: Splits) {
        super.applyTo(receiver)
        density?.applyTo(receiver.density)
    }
}
