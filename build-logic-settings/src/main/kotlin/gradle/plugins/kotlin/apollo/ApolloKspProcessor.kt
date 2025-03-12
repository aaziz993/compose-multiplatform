package gradle.plugins.kotlin.apollo

import kotlinx.serialization.Serializable

@Serializable
internal data class ApolloKspProcessor(
    val schema: String,
    val service: String,
    val packageName: String
)
