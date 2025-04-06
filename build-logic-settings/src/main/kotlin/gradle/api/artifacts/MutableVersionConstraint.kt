package gradle.api.artifacts

import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable

/**
 * A configurable version constraint. This is exposed to the build author, so that one can express
 * more constraints on a module version.
 *
 * @since 4.4
 */
@Serializable
internal data class MutableVersionConstraint(
    override val branch: String? = null,
    override val require: String? = null,
    override val prefer: String? = null,
    override val strictly: String? = null,
    override val reject: List<String>? = null,
    /**
     * Rejects all versions of this component. Can be used to make sure that if such a component is seen in a
     * dependency graph, resolution fails.
     *
     * @since 4.5
     */
    val rejectAll: Boolean? = null,
) : VersionConstraint<org.gradle.api.artifacts.MutableVersionConstraint> {

    override fun applyTo(receiver: org.gradle.api.artifacts.MutableVersionConstraint) {
        receiver::setBranch trySet branch
        receiver::require trySet require
        receiver::prefer trySet prefer
        receiver::strictly trySet strictly
        receiver::reject trySet reject
        receiver::rejectAll trySet rejectAll
    }
}
