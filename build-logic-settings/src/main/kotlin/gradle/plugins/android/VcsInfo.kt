package gradle.plugins.android

import com.android.build.api.dsl.VcsInfo
import gradle.api.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring Version Control information
 */
@Serializable
internal data class VcsInfo(
    /**
     * Determines whether to include VCS info in the build.
     *
     * When the value is not set/null, the feature will be enabled by default in release builds.
     * However, in the case that it is not successful, the build will not fail but will log the
     * error message.
     */
    val include: Boolean? = null
) {

    fun applyTo(info: VcsInfo) {
        info::include trySet include
    }
}
