package plugin.project.kotlin.sqldelight

import gradle.moduleProperties
import gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SqlDelightPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.kotlin.sqldelight.enabled || moduleProperties.targets == null) {
                return@with
            }

            plugins.apply(project.libs.plugins.sqldelight.get().pluginId)

            configureSqlDelightExtension()
        }
    }
}
