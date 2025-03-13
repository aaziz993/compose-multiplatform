package gradle.api.tasks.test

import kotlinx.serialization.Serializable

@Serializable
internal data class Test(
    val className: String,
    val methodName: String
)
