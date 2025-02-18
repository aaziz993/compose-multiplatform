package plugin.project.kotlin.apollo.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ApolloSettings(
    override val generateSourcesDuringGradleSync: Boolean? = null,
    override val linkSqlite: Boolean? = null,
    val enabled: Boolean = true,
) : ApolloExtension
