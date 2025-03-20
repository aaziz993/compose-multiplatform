package gradle.plugins.android

import com.android.build.api.dsl.BaselineProfile
import gradle.api.trySet
import gradle.collection.act
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
    fun applyTo(recipient: BaselineProfile) {
        ignoreFrom?.let(recipient.ignoreFrom::addAll)
        setIgnoreFrom?.act(recipient.ignoreFrom::clear)?.let(recipient.ignoreFrom::addAll)
        recipient::ignoreFromAllExternalDependencies trySet ignoreFromAllExternalDependencies
    }
}
