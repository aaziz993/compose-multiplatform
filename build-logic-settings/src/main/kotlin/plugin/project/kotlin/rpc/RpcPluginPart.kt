package plugin.project.kotlin.rpc

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class RpcPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.kotlin.rpc.enabled
    }

    override fun applyAfterEvaluate() =with(project) {
        plugins.apply(project.libs.plugins.kotlinx.rpc.get().pluginId)

        applySettings()
    }

    private fun applySettings() {
        with(project) {
            configureRpcExtension()
        }
    }
}
