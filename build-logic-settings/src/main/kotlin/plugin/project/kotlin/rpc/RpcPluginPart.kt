package plugin.project.kotlin.rpc

import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class RpcPluginPart: Plugin<Project> {

    override fun apply(target:Project) {
        with(target) {
            if(!moduleProperties.settings.kotlin.rpc.enabled|| moduleProperties.targets.isEmpty()){
                return@with
            }

            plugins.apply(project.libs.plugins.kotlinx.rpc.get().pluginId)

            configureRpcExtension()
        }
    }
}
