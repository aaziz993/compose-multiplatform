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

    override fun equals(other: Any?) =
        super.equals(other) || (other is ResolvedArtifact &&
            (file ?: other.file.absolutePath) == other.file.absolutePath
            && moduleVersion?.equals(other.moduleVersion) != false
            && (name ?: other.name) == other.name
            && (type ?: other.type) == other.type
            && (extension ?: other.extension) == other.extension
            && (classifier ?: other.classifier) == other.classifier
            && id?.equals(other.id) != false
            )

    override fun hashCode(): Int {
        var result = file?.hashCode() ?: 0
        result = 31 * result + (moduleVersion?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (extension?.hashCode() ?: 0)
        result = 31 * result + (classifier?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }
}
