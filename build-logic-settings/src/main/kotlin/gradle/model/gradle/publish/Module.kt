package gradle.model.gradle.publish

import kotlinx.serialization.Serializable

@Serializable
internal data class Module(
    val group: String,
    val moduleName: String
)
