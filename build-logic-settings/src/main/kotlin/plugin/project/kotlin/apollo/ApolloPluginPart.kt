package plugin.project.kotlin.apollo

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class ApolloPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.kotlin.apollo.enabled
    }

    override fun applyAfterEvaluate() = with(project) {
        plugins.apply(project.libs.plugins.apollo3.get().pluginId)

        applySettings()
    }

    private fun applySettings() {
        with(project) {
            configureApolloExtension()
        }
    }
}
