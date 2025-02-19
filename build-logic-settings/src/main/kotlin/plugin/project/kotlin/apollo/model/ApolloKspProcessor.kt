package plugin.project.kotlin.apollo.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ApolloKspProcessor(
    val schema: String,
    val service: String,
    val packageName: String
)
