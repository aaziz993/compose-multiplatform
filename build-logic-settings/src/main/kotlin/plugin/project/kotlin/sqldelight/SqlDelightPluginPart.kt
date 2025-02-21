package plugin.project.kotlin.sqldelight

import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SqlDelightPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.kotlin.sqldelight.enabled || moduleProperties.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.sqldelight.get().pluginId)

            configureSqlDelightExtension()
        }
    }
}
