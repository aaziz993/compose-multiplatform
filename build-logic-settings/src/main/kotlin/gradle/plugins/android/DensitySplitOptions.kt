package gradle.plugins.android

import com.android.build.gradle.internal.dsl.DensitySplitOptions
import kotlinx.serialization.Serializable

@Serializable
internal data class DensitySplitOptions(
    override val isEnable: Boolean? = null,
    override val includes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val reset: Boolean? = null,
    val compatibleScreens: Set<String>? = null,
    val setCompatibleScreens: Set<String>? = null,
) : Split<DensitySplitOptions> {

    override fun applyTo(recipient: DensitySplitOptions) {
        super.applyTo(recipient)

        compatibleScreens?.let { compatibleScreens ->
            recipient.compatibleScreens(*compatibleScreens.toTypedArray())
        }

        setCompatibleScreens?.let(Set<String>::toList)?.let(recipient::setCompatibleScreens)
    }
}
