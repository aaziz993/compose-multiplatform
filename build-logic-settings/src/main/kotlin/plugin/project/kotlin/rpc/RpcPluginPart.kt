package plugin.project.kotlin.rpc

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class RpcPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.plugins.rpc.enabled || projectProperties.kotlin.hasTargets) {
                return@with
            }

            plugins.apply(project.libs.plugins.kotlinx.rpc.get().pluginId)

            configureRpcExtension()
        }
    }
}
