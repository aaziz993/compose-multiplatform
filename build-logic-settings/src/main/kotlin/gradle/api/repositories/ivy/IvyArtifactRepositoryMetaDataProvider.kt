package gradle.api.repositories.ivy

import gradle.reflect.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.repositories.IvyArtifactRepositoryMetaDataProvider

/**
 * The meta-data provider for an Ivy repository. Uses the Ivy module descriptor (`ivy.xml`) to determine the meta-data for module versions and artifacts.
 */
@Serializable
internal data class IvyArtifactRepositoryMetaDataProvider(
    /**
     * Specifies whether dynamic resolve mode should be used for Ivy modules. When enabled, the `revConstraint` attribute for each dependency declaration
     * is used in preference to the `rev` attribute. When disabled (the default), the `rev` attribute is always used.
     */
    val dynamicMode: Boolean? = null,
) {

    fun applyTo(receiver: IvyArtifactRepositoryMetaDataProvider) {
        receiver::setDynamicMode trySet dynamicMode
    }
}
