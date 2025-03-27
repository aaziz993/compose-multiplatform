package gradle.plugins.android.split

import com.android.build.gradle.internal.dsl.DensitySplitOptions
import gradle.api.trySet
import kotlinx.serialization.Serializable

@Serializable
internal data class DensitySplitOptions(
    override val isEnable: Boolean? = null,
    override val includes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val reset: Boolean? = null,
    val compatibleScreens: Set<String>? = null,
    val setCompatibleScreens: Set<String>? = null,
) : SplitOptions<DensitySplitOptions>() {

    override fun applyTo(receiver: DensitySplitOptions) {
        super<SplitOptions>.applyTo(receiver)

        compatibleScreens?.toTypedArray()?.let(receiver::compatibleScreens)
        receiver.compatibleScreens trySet setCompatibleScreens
    }
}
