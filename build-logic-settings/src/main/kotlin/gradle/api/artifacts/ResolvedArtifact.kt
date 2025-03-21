package gradle.api.artifacts

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ResolvedArtifact

@Serializable
internal data class ResolvedArtifact(
    val file: String? = null,
    val moduleVersion: ResolvedModuleVersion? = null,
    val name: String? = null,
    val type: String? = null,
    val extension: String? = null,
    val classifier: String? = null,
    val id: ComponentArtifactIdentifier? = null,
) {

    fun equals(other: ResolvedArtifact) =
        (file ?: other.file.absolutePath) == other.file.absolutePath
            && moduleVersion?.equals(other.moduleVersion) != false
            && (name ?: other.name) == other.name
            && (type ?: other.type) == other.type
            && (extension ?: other.extension) == other.extension
            && (classifier ?: other.classifier) == other.classifier
            && id?.equals(other.id) != false
}
