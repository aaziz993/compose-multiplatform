package gradle.plugins.android.split

import com.android.build.gradle.internal.dsl.DensitySplitOptions
import gradle.collection.act
import kotlin.collections.addAll
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

    override fun applyTo(recipient: DensitySplitOptions) {
        super<SplitOptions>.applyTo(recipient)
        super<DensitySplit>.applyTo(recipient)

        compatibleScreens?.let { compatibleScreens ->
            recipient.compatibleScreens(*compatibleScreens.toTypedArray())
        }

        setCompatibleScreens?.act(recipient.compatibleScreens::clear)?.let(recipient.compatibleScreens::addAll)
    }
}
