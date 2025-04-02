package gradle.api.artifacts

import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ModuleDependencyCapabilitiesHandler
import org.gradle.api.initialization.Settings

/**
 * The capabilities requested for a dependency. This is used in variant-aware dependency
 * management, to select only variants which provide the requested capabilities. By
 * default, Gradle will only look for variants which provide the "implicit" capability,
 * which corresponds to the GAV coordinates of the component. If the user calls methods
 * on this handler, then the requirements change and explicit capabilities are required.
 *
 * @since 5.3
 */
@Serializable
internal data class ModuleDependencyCapabilitiesHandler(
    /**
     * Requires multiple capabilities. The selected variants MUST provide ALL of them
     * to be selected.
     *
     * @param capabilityNotations the capability [notations][ConfigurationPublications.capability] (e.g. group:name:version), [Providers][Provider] of any notation are also accepted
     */
    val requireCapabilities: List<Dependency>? = null,
    /**
     * Require a capability of a component based on the name of the feature provided by the component.
     *
     *
     * A capability is derived from a feature based on the module identity of the component that a dependency
     * resolves to. For example, variant of a component with module identity 'group:name:version' that provides
     * a feature named 'test-fixtures' would have a capability 'group:name-test-fixtures:version'.
     *
     * @param featureName The name of the feature to require
     *
     * @since 8.11
     */
    val requireFeature: String? = null,
) {

    context(Settings)
    @Suppress("UnstableApiUsage")
    fun applyTo(receiver: ModuleDependencyCapabilitiesHandler) {
        receiver::requireCapabilities trySet requireCapabilities?.map { requireCapability ->
            requireCapability.resolve()
        }

        requireFeature?.let(receiver::requireFeature)
    }
}
