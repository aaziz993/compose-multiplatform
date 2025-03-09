package gradle.model.kotlin.kmp.jvm.android

import com.android.build.gradle.internal.dsl.DensitySplitOptions
import kotlinx.serialization.Serializable

@Serializable
internal data class DensitySplitOptions(
    override val isEnable: Boolean? = null,
    override val includes: List<String>? = null,
    override val excludes: List<String>? = null,
    override val reset: Boolean? = null,
    val compatibleScreens: List<String>? = null,
) : Split {

    fun applyTo(options: DensitySplitOptions) {
        super.applyTo(options)
        compatibleScreens?.let(options::setCompatibleScreens)
    }
}
