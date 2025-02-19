package plugin.project.kotlin.ktorfit

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class KtorfitPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.kotlin.ktorfit.enabled
    }

    override fun applyAfterEvaluate() =with(project) {
        plugins.apply(project.libs.plugins.ktorfit.get().pluginId)

        applySettings()
    }

    private fun applySettings() {
        with(project) {
            configureKtorfitGradleConfiguration()
        }
    }
}
