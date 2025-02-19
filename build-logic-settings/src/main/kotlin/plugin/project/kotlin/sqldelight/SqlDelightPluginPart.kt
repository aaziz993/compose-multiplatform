package plugin.project.kotlin.sqldelight

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class SqlDelightPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.kotlin.sqldelight.enabled
    }

    override fun applyAfterEvaluate() = with(project) {
        plugins.apply(project.libs.plugins.sqldelight.get().pluginId)

        applySettings()
    }

    private fun applySettings() {
        with(project) {
            configureSqlDelightExtension()
        }
    }
}
