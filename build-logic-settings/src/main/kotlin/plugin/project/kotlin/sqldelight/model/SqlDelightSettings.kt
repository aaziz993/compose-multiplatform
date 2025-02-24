package plugin.project.kotlin.sqldelight.model

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import gradle.sqldelight
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.model.EnabledSettings

@Serializable
internal data class SqlDelightSettings(
    override val databases: List<SqlDelightDatabase>? = null,
    override val linkSqlite: Boolean? = null,
    override val enabled: Boolean = false
) : SqlDelightExtension, EnabledSettings {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("sqldelight").id) {
            super.applyTo(sqldelight)
        }
}
