package gradle.plugins.android.compile

import kotlinx.serialization.Serializable

@Serializable
internal data class CompileSdkAddon(
    val vendor: String,
    val name: String,
    val version: Int
)
