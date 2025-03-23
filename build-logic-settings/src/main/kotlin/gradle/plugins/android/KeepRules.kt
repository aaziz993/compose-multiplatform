package gradle.plugins.android

import com.android.build.api.dsl.KeepRules
import kotlinx.serialization.Serializable

/**
 * DSL object for external library dependencies keep rules configurations.
 */
@Serializable
internal data class KeepRules(
    /**
     * Ignore keep rules from listed external dependencies. External dependencies can be specified
     * via GAV coordinates(e.g. "groupId:artifactId:version") or in the format of
     * "groupId:artifactId" in which case dependencies are ignored as long as they match
     * groupId & artifactId.
     */
    val ignoreFrom: Set<String>? = null,
    /**
     * Ignore keep rules from all the external dependencies.
     */
    val ignoreFromAllExternalDependencies: Boolean? = null
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(receiver: KeepRules) {
        ignoreFrom?.toTypedArray()?.let(receiver::ignoreFrom)

        ignoreFromAllExternalDependencies?.let(receiver::ignoreFromAllExternalDependencies)
    }
}
