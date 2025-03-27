package gradle.api.publish.maven

import gradle.act
import gradle.actIfTrue
import gradle.takeIfTrue
import kotlinx.serialization.Serializable
import org.gradle.api.publish.VariantVersionMappingStrategy

/**
 * Defines the version mapping strategy when publishing, for a specific variant.
 *
 * @since 5.2
 */
@Serializable
internal data class VariantVersionMappingStrategy(
    /**
     * Declares that this variant should use versions from the resolution
     * of a default configuration chosen by Gradle.
     */
    val fromResolutionResult: Boolean? = null,
    /**
     * Declares that this variant should use versions from the resolution
     * of the configuration provided as an argument.
     *
     * @param configurationName a resolvable configuration name where to pick resolved version numbers
     */
    val fromResolutionOf: String? = null,
) {

    fun applyTo(receiver: VariantVersionMappingStrategy) {
        fromResolutionResult?.actIfTrue(receiver::fromResolutionResult)
        fromResolutionOf?.let(receiver::fromResolutionOf)
    }
}
