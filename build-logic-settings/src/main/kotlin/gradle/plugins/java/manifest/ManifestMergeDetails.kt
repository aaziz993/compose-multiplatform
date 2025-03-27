package gradle.plugins.java.manifest

import gradle.actIfTrue
import kotlinx.serialization.Serializable
import org.gradle.api.java.archives.ManifestMergeDetails

@Serializable
internal data class ManifestMergeDetails(
    val section: String? = null,
    val key: String? = null,
    val baseValue: String? = null,
    val mergeValue: String? = null,
    val oldValue: String? = null,
    val value: String? = null,
    val exclude: Boolean? = null,
) {

    fun equals(other: ManifestMergeDetails) =
        (section ?: other.section) == other.section
            && (key ?: other.key) == other.key
            && (baseValue ?: other.baseValue) == other.baseValue
            && (mergeValue ?: other.mergeValue) == other.mergeValue
            && (oldValue ?: other.value) == other.value

    fun applyTo(receiver: ManifestMergeDetails) {
        value?.let(receiver::setValue)
        exclude?.actIfTrue(receiver::exclude)
    }
}
