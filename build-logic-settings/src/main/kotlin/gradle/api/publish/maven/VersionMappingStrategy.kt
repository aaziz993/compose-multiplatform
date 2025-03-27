package gradle.api.publish.maven

import gradle.api.tryApply
import kotlinx.serialization.Serializable
import org.gradle.api.publish.VersionMappingStrategy

/**
 * The version mapping strategy for a publication. By default,
 * Gradle will use the declared versions of a dependency directly. However
 * in some situations it might be better to publish the resolved versions, or both
 * when the metadata format supports it.
 *
 * @since 5.2
 */
@Serializable
internal data class VersionMappingStrategy(
    /**
     * Configures the version mapping strategy for all variants
     * @param action the configuration action
     */
    val allVariants: VariantVersionMappingStrategy? = null,
    /**
     * A short hand method to configure the variants which matches the provided Usage attribute.
     * This is the recommended way to configure the mapping strategy for the general case.
     * @param usage the usage to look for
     * @param action the configuration action
     */
    val usages: List<Usage>? = null,
) {

    fun applyTo(receiver: VersionMappingStrategy) {
        receiver::allVariants tryApply allVariants?.let{ allVariants -> allVariants::applyTo }
        
        usages?.forEach { (usage, strategy) ->
            receiver.usage(usage, strategy::applyTo)
        }
    }
}
