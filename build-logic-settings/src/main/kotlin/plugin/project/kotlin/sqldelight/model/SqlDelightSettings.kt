package plugin.project.kotlin.sqldelight.model

import gradle.id
import gradle.libs
import gradle.model.kotlin.sqldelight.SqlDelightDatabase
import gradle.model.kotlin.sqldelight.SqlDelightExtension
import gradle.model.project.EnabledSettings
import gradle.plugin
import gradle.plugins
import gradle.settings
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
