package gradle.plugins.kotlin.sqldelight.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.kotlin.sqldelight.SqlDelightDatabase
import gradle.plugins.kotlin.sqldelight.SqlDelightDatabaseKeyTransformingSerializer
import gradle.plugins.kotlin.sqldelight.SqlDelightExtension
import gradle.plugins.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class SqlDelightSettings(
    override val databases: LinkedHashSet<@Serializable(with = SqlDelightDatabaseKeyTransformingSerializer::class) SqlDelightDatabase>? = null,
    override val linkSqlite: Boolean? = null,
    override val enabled: Boolean = false
) : SqlDelightExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("sqldelight").id) {
            super.applyTo()
        }
}
