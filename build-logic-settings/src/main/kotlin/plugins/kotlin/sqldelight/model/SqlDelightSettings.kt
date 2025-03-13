package plugins.kotlin.sqldelight.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.kotlin.sqldelight.SqlDelightDatabase
import gradle.plugins.kotlin.sqldelight.SqlDelightExtension
import gradle.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class SqlDelightSettings(
    override val databases: List<SqlDelightDatabase>? = null,
    override val linkSqlite: Boolean? = null,
    override val enabled: Boolean = false
) : SqlDelightExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("sqldelight").id) {
            super.applyTo()
        }
}
