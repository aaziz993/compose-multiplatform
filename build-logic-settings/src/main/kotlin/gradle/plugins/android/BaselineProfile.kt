package gradle.plugins.android

import com.android.build.api.dsl.BaselineProfile
import gradle.api.tryAddAll
import gradle.api.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object in [Optimization] for configuring properties related to Baseline Profiles.
 */
@Serializable
internal data class BaselineProfile(
    /**
     * Ignore baseline profiles from listed external dependencies. External dependencies can be
     * specified via GAV coordinates(e.g. "groupId:artifactId:version") or in the format of
     * "groupId:artifactId" in which case dependencies are ignored as long as they match
     * groupId & artifactId.
     */
    val ignoreFrom: Set<String>? = null,
    val setIgnoreFrom: Set<String>? = null,

    /**
     * Ignore baseline profiles from all the external dependencies.
     */
    val ignoreFromAllExternalDependencies: Boolean? = null,
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(receiver: BaselineProfile) {
        receiver.ignoreFrom tryAddAll ignoreFrom
        receiver.ignoreFrom trySet setIgnoreFrom
        receiver::ignoreFromAllExternalDependencies trySet ignoreFromAllExternalDependencies
    }
}
