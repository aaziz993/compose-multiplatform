package plugin.project.java.model

import kotlinx.serialization.Serializable

@Serializable
internal data class JvmVendorSpec(
    val matches: String? = null,
)
