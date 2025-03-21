package gradle.plugins.java.manifest

import kotlinx.serialization.Serializable
import org.gradle.api.java.archives.ManifestMergeSpec

@Serializable
internal data class ManifestMergeSpec(
    val contentCharset: String? = null,
    val from: Set<String>? = null,
    val eachEntries: List<ManifestMergeDetails>? = null,
) {

    fun applyTo(recipient: ManifestMergeSpec) {
        contentCharset?.let(recipient::setContentCharset)
        from?.toTypedArray()?.let(recipient::from)

        eachEntries?.let { eachEntries ->
            recipient.eachEntry {
                eachEntries.find { eachEntry -> eachEntry.equals(this) }?.applyTo(this)
            }
        }
    }
}
