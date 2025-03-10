package gradle.model.gradle.publish.repository

import kotlinx.serialization.Serializable

@Serializable
internal data class Module(
    val group: String,
    val moduleName: String
)
