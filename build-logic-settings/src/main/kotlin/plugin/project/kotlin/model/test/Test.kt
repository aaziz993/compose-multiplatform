package plugin.project.kotlin.model.test

import kotlinx.serialization.Serializable

@Serializable
internal data class Test(
    val className: String,
    val methodName: String
)
