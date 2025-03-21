package gradle.plugins.java.manifest

import kotlinx.serialization.Serializable

@Serializable
internal data class From(
    val mergePath: String,
    val mergeSpec: ManifestMergeSpec,
)
