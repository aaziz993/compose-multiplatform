package gradle.api.artifacts

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ModuleDependencyCapabilitiesHandler

/**
 * Allows configuring the variant-aware selection aspects of a specific
 * dependency. This includes the ability to substitute a dependency on
 * a platform with another platform, or substitute a dependency without
 * attributes with a dependency with attributes.
 *
 * @since 6.6
 */
@Serializable
internal data class VariantSelectionDetails(
    /**
     * Selects the platform variant of a component
     */
    val platform: Boolean? = null,
    /**
     * Selects the enforced platform variant of a component
     */
    val enforcedPlatform: Boolean? = null,
    /**
     * Selects the library variant of a component
     */
    val library: Boolean? = null,
    /**
     * Replaces the provided selector capabilities with the capabilities configured
     * via the configuration action.
     * @param configurationAction the configuration action
     */
    val capabilities: ModuleDependencyCapabilitiesHandler? = null,
)
