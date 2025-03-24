package gradle.plugins.android.split

import com.android.build.gradle.internal.dsl.AbiSplitOptions
import kotlinx.serialization.Serializable

@Serializable
internal data class AbiSplitOptions(
    override val isEnable: Boolean? = null,
    override val includes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val reset: Boolean? = null,
    override val isUniversalApk: Boolean? = null,
) : SplitOptions<AbiSplitOptions>(), AbiSplit<AbiSplitOptions>
