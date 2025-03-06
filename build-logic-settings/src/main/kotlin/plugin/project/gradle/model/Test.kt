package plugin.project.kotlin.kmp.model.test

import kotlinx.serialization.Serializable

@Serializable
internal data class Test(
    val className: String,
    val methodName: String
)
