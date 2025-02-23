package plugin.project.kotlin.sqldelight.model

import kotlinx.serialization.Serializable

@Serializable
internal data class SqlDelightSettings(
    override val databases: List<SqlDelightDatabase>? = null,
    override val linkSqlite: Boolean? = null,
    val enabled: Boolean = true
) : SqlDelightExtension
