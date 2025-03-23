package gradle.plugins.java.manifest

import kotlinx.serialization.Serializable
import org.gradle.api.java.archives.ManifestMergeSpec

@Serializable
internal data class ManifestMergeSpec(
    val contentCharset: String? = null,
    val from: Set<String>? = null,
    val eachEntries: List<ManifestMergeDetails>? = null,
) {

    fun applyTo(receiver: ManifestMergeSpec) {
        contentCharset?.let(receiver::setContentCharset)
        from?.toTypedArray()?.let(receiver::from)

        eachEntries?.let { eachEntries ->
            receiver.eachEntry {
                eachEntries.find { eachEntry -> eachEntry.equals(this) }?.applyTo(this)
            }
        }
    }
}
